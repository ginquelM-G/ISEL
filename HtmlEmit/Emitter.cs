using System;
using System.Collections.Generic;
using System.Reflection;
using System.Reflection.Emit;

namespace HtmlEmit
{
    public class HtmlIgnoreAttribute : Attribute
    {

    }

    public class HtmlAsAttribute : Attribute
    {
        public HtmlAsAttribute(string html)
        {
            Html = html;
        }

        public string Html { get; private set; }
    }


    interface IHtml
    {
        string Html(object target);
    }

    public abstract class AbstractHtml : IHtml
    {
        public static string Format(string name, object val)
        {
            string template = "<li class='list-group-item'><strong>{0}</strong>: {1}</li>";
            return String.Format(template, name, val);
        }
        public static string Format(string name, object[] arr)
        {
            string str = name + ": [";
            for (int i = 0; i < arr.Length; i++)
            {
                //str += Emitter.ObjFieldsToString(arr[i], "<td>{0}</td>") + ", ";
            }
            return str + "]";
        }

        public abstract string Html(object target);
    }


    public class Emitter
    {
        static readonly MethodInfo formatterForObject = typeof(AbstractHtml).GetMethod("Format", new Type[] { typeof(string), typeof(object) });
        static readonly MethodInfo concat = typeof(string).GetMethod("Concat", new Type[] { typeof(string), typeof(string) });

        static Dictionary<Type, IHtml> cachedTypes = new Dictionary<Type, IHtml>();
        internal static string ObjFieldsToString(object obj)
        {
            IHtml logger;
            Type klass = obj.GetType();
            if (!cachedTypes.TryGetValue(klass, out logger))
            {
                logger = EmitHtml(klass);
                cachedTypes.Add(klass, logger);
            }
            return logger.Html(obj);
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
            PropertyInfo[] ps = klass.GetProperties(BindingFlags.Public | BindingFlags.Instance);

            LocalBuilder target = il.DeclareLocal(klass);
            il.Emit(OpCodes.Ldarg_1);          // push target
            il.Emit(OpCodes.Castclass, klass); // castclass
            il.Emit(OpCodes.Stloc, target);    // store on local variable 
            
            il.Emit(OpCodes.Ldstr, ""); //str
            foreach (PropertyInfo p in ps)
            {
                if (p.GetCustomAttribute(typeof(HtmlIgnoreAttribute)) != null) continue;
                object attr = p.GetCustomAttribute(typeof(HtmlAsAttribute), true);
                if (attr == null)
                {
                    //noncacheableattr
                    //str+=format(p.Name, p.GetValue(target))

                    il.Emit(OpCodes.Ldstr, p.Name);
                    il.Emit(OpCodes.Ldloc, target);    // ldloc target
                    //il.Emit(OpCodes.Ldstr, "propriedade normal");
                    //il.Emit(OpCodes.Callvirt, klass.GetProperty(p.Name, BindingFlags.Instance | BindingFlags.Public).GetGetMethod(true));
                    il.Emit(OpCodes.Call, formatterForObject);
                } else
                {
                    //link
                    /*string html = ((HtmlAsAttribute)attr).Html;
                    html.Replace("{name}", p.Name).Replace("{value}", p.GetValue(target).ToString());
                    il.Emit(OpCodes.Ldstr, html);*/
                    il.Emit(OpCodes.Ldstr, "custom attr");
                    il.Emit(OpCodes.Ldloc, target);    // ldloc target
                    il.Emit(OpCodes.Call, formatterForObject);
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
            string template = "<ul class='list-group'>{0}</ul>";
            return String.Format(template, ObjFieldsToString(obj));
        }

        public string ToHtml(object[] arr)
        {

            string table = "<table class='table table-hover'>";
            string thead = "<thead>";
            string tbody = "<tbody>";
            bool first = true;

            foreach (object o in arr)
            {
                Type t = o.GetType();
                //string td = "<td>{0}</td>";
                string th = "<th>{0}</th>";

                if (first)
                {
                    thead += "<tr>";
                    foreach (PropertyInfo p in t.GetProperties())
                    {
                        thead += String.Format(th, p.Name);
                    }
                    thead += "</tr></thead>";
                    first = false;
                }

                //tbody += "<tr>" + Cache.CacheableArrToString(o, td) + "</tr>";
            }
            return table + thead + tbody + "</tbody>" + "</table>";
        }
    }
}
