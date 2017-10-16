using System;
using System.Collections.Generic;
using System.Reflection;

namespace HtmlReflect
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

        public string Html { get ; private set; }
    }


    interface ICacheable
    {
        string GetValueAsString(object target);
        string GetValueAsString(object obj, string format);
    }

    class CacheableHtmlAs : ICacheable
    {
        PropertyInfo p;
        string html;
        public CacheableHtmlAs(PropertyInfo p, string html)
        {
            this.p = p;
            this.html = html;
        }

        public string GetValueAsString(object target)
        {
            return html.Replace("{name}", p.Name).Replace("{value}", p.GetValue(target).ToString());
        }

        public string GetValueAsString(object obj, string format)
        {
            return String.Format(format, GetValueAsString(obj));
        }
    }

    class CacheableNonCustom : ICacheable
    {
        PropertyInfo p;
        public CacheableNonCustom(PropertyInfo p)
        {
            this.p = p;
        }

        public string GetValueAsString(object target)
        {
            string template = "<li class='list-group-item'><strong>{0}</strong>: {1}</li>";
            return String.Format(template, p.Name, p.GetValue(target));
        }

        public string GetValueAsString(object obj, string format)
        {   if (format == null) return GetValueAsString(obj);
            return String.Format(format, p.GetValue(obj));
        }
    }

    public class Cache
    {
        static Dictionary<Type, List<ICacheable>> cacheableTypes = new Dictionary<Type, List<ICacheable>>();

        static List<ICacheable> GetCacheableProps(Type klass)
        {
            List<ICacheable> res;
            if (cacheableTypes.TryGetValue(klass, out res)) return res;
            PropertyInfo[] pf = klass.GetProperties(BindingFlags.Public | BindingFlags.Instance);
            res = new List<ICacheable>();
            foreach (PropertyInfo p in pf)
            {
                if (p.GetCustomAttribute(typeof(HtmlIgnoreAttribute), true) != null) continue;
                object[] attrs = p.GetCustomAttributes(typeof(HtmlAsAttribute), true);
                if (attrs.Length == 0)
                {
                    res.Add(new CacheableNonCustom(p));
                }
                else
                {
                    string html = ((HtmlAsAttribute)attrs[attrs.Length - 1]).Html;
                    res.Add(new CacheableHtmlAs(p, html));
                }
            }
            cacheableTypes.Add(klass, res);
            return res;
        }

        public static string CacheableToString(object obj)
        {
            String str = "";
            List<ICacheable> fs = GetCacheableProps(obj.GetType());
            foreach (ICacheable p in fs)
            {
                str += p.GetValueAsString(obj);
            }
            return str;
        }
        public static string CacheableArrToString(object obj, string format)
        {
            String str = "";
            List<ICacheable> fs = GetCacheableProps(obj.GetType());
            foreach (ICacheable p in fs)
            {
                str += p.GetValueAsString(obj, format);
            }
            return str;
        }
    }

    


    public class Htmlect
    {
        public string ToHtml(object obj)
        {
            string str = "<ul class='list-group'>";
           
            return str+= Cache.CacheableToString(obj) + "</ul>";
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
                string td = "<td>{0}</td>";
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
                
                tbody += "<tr>" + Cache.CacheableArrToString(o, td) + "</tr>";
            }
            return table + thead + tbody + "</tbody>" + "</table>";
        }
    }
}
