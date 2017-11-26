using HtmlReflect;
using System;
using System.Collections.Generic;
using System.Reflection;
using System.Reflection.Emit;

namespace HtmlEmit
{
    interface IHtml
    {
        string Html(object target);
    }

    public abstract class AbstractHtml : IHtml
    {
        public static string Format(string name, object val)
        {
            string template = "<li class='list-group-item'><strong>{0}</strong>: {1}</li>";
            return String.Format(template, name, val != null ? val.ToString() : "");
        }

        public static string FormatAttr(string name, object val, string format)
        {
            if (val == null) return "";
            return format.Replace("{name}", name).Replace("{value}", val.ToString());
        }

        public static string FormatArr(string name, object value, string attr)
        {
            string td = "<td>{0}</td>";
            string val = value == null ? "" : value.ToString();
            if (attr == null) return String.Format(td, val);
            return String.Format(td, attr.Replace("{value}", val));
        }

        public abstract string Html(object target);

    }


    public class Emitter
    {
        static readonly MethodInfo formatterForObject = typeof(AbstractHtml).GetMethod("Format", new Type[] { typeof(string), typeof(object) });
        static readonly MethodInfo formatterForAttr = typeof(AbstractHtml).GetMethod("FormatAttr", new Type[] { typeof(string), typeof(object), typeof(string) });
        static readonly MethodInfo formatterForArray = typeof(AbstractHtml).GetMethod("FormatArr", new Type[] { typeof(string), typeof(object), typeof(string) });
        static readonly MethodInfo concat = typeof(string).GetMethod("Concat", new Type[] { typeof(string), typeof(string) });

        static Dictionary<Type, IHtml> cachedTypes = new Dictionary<Type, IHtml>();
        internal static IHtml ObjPropsToString(object obj)
        {
            IHtml objHtml;
            Type klass = obj.GetType();
            if (!cachedTypes.TryGetValue(klass, out objHtml))
            {
                objHtml = EmitHtml(klass);
                cachedTypes.Add(klass, objHtml);
            }
            return objHtml;
        }

        private static IHtml EmitHtml(Type klass)
        {
            AssemblyName aName = new AssemblyName("DynamicEmitter_" + klass.Name);
            AssemblyBuilder ab =
                AppDomain.CurrentDomain.DefineDynamicAssembly(
                    aName,
                    AssemblyBuilderAccess.RunAndSave);

            ModuleBuilder mb = ab.DefineDynamicModule(aName.Name, aName.Name + ".dll");

            TypeBuilder tb = mb.DefineType(
                "Html" + klass.Name,
                TypeAttributes.Public,
                typeof(AbstractHtml));

            MethodBuilder methodBuilder = tb.DefineMethod(
                "Html",
                MethodAttributes.Public | MethodAttributes.Virtual | MethodAttributes.ReuseSlot,
                typeof(string),
                new Type[] { typeof(object) }
                );

            ILGenerator il = methodBuilder.GetILGenerator();
            
            Type elementType = klass;
            if (klass.IsArray)
                elementType = klass.GetElementType();
            LocalBuilder target = il.DeclareLocal(elementType);
            il.Emit(OpCodes.Ldarg_1);
            il.Emit(OpCodes.Castclass, elementType);
            il.Emit(OpCodes.Stloc, target);

            PropertyInfo[] ps = elementType.GetProperties(BindingFlags.Public | BindingFlags.Instance);

            il.Emit(OpCodes.Ldstr, ""); //str
            foreach (PropertyInfo p in ps)
            {
                if (p.GetCustomAttribute(typeof(HtmlIgnoreAttribute)) != null) continue;
                object attr = p.GetCustomAttribute(typeof(HtmlAsAttribute), true);

                il.Emit(OpCodes.Ldstr, p.Name);
                il.Emit(OpCodes.Ldloc, target);
                il.Emit(OpCodes.Callvirt, p.GetGetMethod());
                if (p.PropertyType.IsValueType)
                    il.Emit(OpCodes.Box, p.PropertyType);

                if (klass.IsArray)
                {
                    if (attr == null)
                    {
                        il.Emit(OpCodes.Ldnull);
                    }
                    else
                    {
                        string html = ((HtmlAsAttribute)attr).Html;
                        il.Emit(OpCodes.Ldstr, html);
                    }
                    il.Emit(OpCodes.Call, formatterForArray);
                }
                else
                {
                    if (attr == null)
                    {
                        //string
                        il.Emit(OpCodes.Call, formatterForObject);
                    }
                    else
                    {
                        // attr
                        string html = ((HtmlAsAttribute)attr).Html;
                        il.Emit(OpCodes.Ldstr, html);
                        il.Emit(OpCodes.Call, formatterForAttr);
                    }
                }
                il.Emit(OpCodes.Call, concat);
            }
            
            il.Emit(OpCodes.Ret);              // ret
            
            Type t = tb.CreateType();
            ab.Save(aName.Name + ".dll");
            return (IHtml)Activator.CreateInstance(t);
        }

        public string ToHtml(object obj)
        {
            string ul = "<ul class='list-group'>{0}</ul>";
            string lis = ObjPropsToString(obj).Html(obj);
            return String.Format(ul, lis);
        }

        public string ToHtml(object[] arr)
        {
            string table = "<table class='table table-hover'>{0}{1}</table>";
            string thead = ConstructTableHead(arr[0].GetType());
            string tr = "<tr>{0}</tr>";

            string tbody = "<tbody>";
            IHtml emit = ObjPropsToString(arr);
            foreach (object o in arr)
            {
                tbody += String.Format(tr, emit.Html(o));
            }
            tbody += "</tbody>";
            return String.Format(table, thead, tbody);
        }

        private string ConstructTableHead(Type type)
        {
            string str = "<thead><tr>";
            string th = "<th>{0}</th>";

            foreach (PropertyInfo p in type.GetProperties())
            {
                if (p.GetCustomAttribute(typeof(HtmlIgnoreAttribute)) != null) continue;
                str += String.Format(th, p.Name);
            }
            return str + "</tr></thead>";
        }
    }
}
