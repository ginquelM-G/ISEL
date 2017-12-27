using HtmlReflect;
using System;
using System.Collections.Generic;
using System.Reflection;
using System.Reflection.Emit;

namespace HtmlEmit
{
    interface IHtml
    {
        string Thead(object target);
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
        public abstract string Thead(object target);
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
            if (!cachedTypes.TryGetValue(klass, out objHtml) &&
                !cachedTypes.TryGetValue(typeof(IEnumerable<>), out objHtml) &&
                !cachedTypes.TryGetValue(typeof(Func<>), out objHtml))
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

            MethodBuilder mBuilder = tb.DefineMethod(
                "Thead",
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
            if (!klass.IsArray)
            {
                il.Emit(OpCodes.Ldstr, "<ul class='list-group'>"); //str
            } else {
                il.Emit(OpCodes.Ldstr, "<tr>"); //str
            }
            
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

            if (!klass.IsArray)
            {
                il.Emit(OpCodes.Ldstr, "</ul>"); //str
            }
            else
            {
                il.Emit(OpCodes.Ldstr, "</tr>"); //str
            }
            il.Emit(OpCodes.Call, concat);
            il.Emit(OpCodes.Ret);              // ret

            ILGenerator ilGen = mBuilder.GetILGenerator();
            ilGen.Emit(OpCodes.Ldstr, "<thead><tr>");
            foreach (PropertyInfo p in ps)
            {
                if (p.GetCustomAttribute(typeof(HtmlIgnoreAttribute)) != null) continue;
                ilGen.Emit(OpCodes.Ldstr, "<th>");
                ilGen.Emit(OpCodes.Call, concat);
                ilGen.Emit(OpCodes.Ldstr, p.Name);
                ilGen.Emit(OpCodes.Call, concat);
                ilGen.Emit(OpCodes.Ldstr, "</th>");
                ilGen.Emit(OpCodes.Call, concat);
            }
            ilGen.Emit(OpCodes.Ldstr, "</tr></thead>");
            ilGen.Emit(OpCodes.Call, concat);
            ilGen.Emit(OpCodes.Ret);              // ret

            Type t = tb.CreateType();
            ab.Save(aName.Name + ".dll");
            return (IHtml)Activator.CreateInstance(t);


            

        }

        public string ToHtml(object obj)
        {
            return ObjPropsToString(obj).Html(obj);
        }

        public string ToHtml<T>(IEnumerable<T> arr)
        {
            IHtml emit = ObjPropsToString(arr);

            if (emit.GetType() == typeof(HtmlFormatterForSequenceOf<T>))
            {
                return emit.Html(arr);
            }
            string table = "<table class='table table-hover'>{0}{1}</table>";
            string tbody = "<tbody>";

            string thead = emit.Thead(typeof(T));
            foreach (T o in arr)
            {
                tbody += emit.Html(o);
            }
            tbody += "</tbody>";

            return String.Format(table, thead, tbody);
        }

    

        public Emitter ForTypeDetails<T>(Func<T, string> formatter)
        {
            Type t = typeof(T);
            if (cachedTypes.ContainsKey(t))
                cachedTypes[t] = new HtmlFormatterForTypeDetails<T>(formatter);
            else
                cachedTypes.Add(t, new HtmlFormatterForTypeDetails<T>(formatter));
            return this;
        }

        public Emitter ForTypeInTable<T>(IEnumerable<string> headers, Func<T, string> transf)
        {
            Type t = typeof(IEnumerable<>);
            cachedTypes.Add(t, new HtmlFormatterTypeInTable<T>(headers, transf));
            return this;
        }

        public Emitter ForSequenceOf<T>(Func<IEnumerable<T>, string> transf)
        {
            Type t = typeof(Func<>);
            if (cachedTypes.ContainsKey(t))
                cachedTypes[t] = new HtmlFormatterForSequenceOf<T>(transf);
            else
                cachedTypes.Add(t, new HtmlFormatterForSequenceOf<T>(transf));
            return this;
        }

        private class HtmlFormatterForTypeDetails<T> : IHtml
        {

            Func<T, string> formatter;

            public HtmlFormatterForTypeDetails(Func<T, string> f)
            {
                formatter = f;
            }

            public string Html(object o)
            {
                return formatter((T)o);
            }

            public string Thead(object target)
            {
                throw new NotImplementedException();
            }
        }

        private class HtmlFormatterTypeInTable<T> : IHtml
        {
            private string thead = "<thead>{0}</thead>";
            private Func<T, string> transf;

            public HtmlFormatterTypeInTable(IEnumerable<string> headers, Func<T, string> transf)
            {
                string h = "";
                foreach (string s in headers)
                    h += String.Format("<th>{0}</th>", s);
                h = String.Format("<tr>{0}</tr>", h);
                this.thead = String.Format(thead, h);
                this.transf = transf;
            }

            public string Html(object target)
            {
                return transf((T)target);
            }

            public string Thead(object target)
            {
                return thead;
            }
        }

        private class HtmlFormatterForSequenceOf<T> : IHtml
        {
            private readonly Func<IEnumerable<T>, string> transf;

            public HtmlFormatterForSequenceOf(Func<IEnumerable<T>, string> transf)
            {
                this.transf = transf;
            }

            public string Html(object target)
            {
                return transf((IEnumerable<T>)target);
            }

            public string Thead(object target)
            {
                return "";
            }
        }
    }
}
