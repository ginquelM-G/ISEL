﻿using HtmlReflect;
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

        public static string Format(string name, object val, string format)
        {
            return format.Replace("{name}", name).Replace("{value}", val.ToString());
        }

        public static string Format(object[] arr)
        {
            string str = "[";
            for (int i = 0; i < arr.Length; i++)
            {
                str += Emitter.ObjFieldsToString(arr[i]);
            }
            return str + "]";
        }

        public abstract string Html(object target);
        
    }


    public class Emitter
    {
        static readonly MethodInfo formatterForObject = typeof(AbstractHtml).GetMethod("Format", new Type[] { typeof(string), typeof(object) });
        static readonly MethodInfo formatterForAttr = typeof(AbstractHtml).GetMethod("Format", new Type[] { typeof(string), typeof(object), typeof(string) });
        static readonly MethodInfo formatterForArray = typeof(AbstractHtml).GetMethod("Format", new Type[] { typeof(object[]) });
        static readonly MethodInfo concat = typeof(string).GetMethod("Concat", new Type[] { typeof(string), typeof(string) });

        static Dictionary<Type, IHtml> cachedTypes = new Dictionary<Type, IHtml>();
        internal static string ObjFieldsToString(object obj)
        {
            IHtml objHtml;
            Type klass = obj.GetType();
            if (!cachedTypes.TryGetValue(klass, out objHtml))
            {
                objHtml = EmitHtml(klass);
                cachedTypes.Add(klass, objHtml);
            }
            return objHtml.Html(obj);
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
            
            if(!klass.IsArray)
            {
                il.Emit(OpCodes.Ldstr, ""); //str
                foreach (PropertyInfo p in ps)
                {
                    if (p.GetCustomAttribute(typeof(HtmlIgnoreAttribute)) != null) continue;
                    object attr = p.GetCustomAttribute(typeof(HtmlAsAttribute), true);
                    if (attr == null)
                    {
                        //string

                        il.Emit(OpCodes.Ldstr, p.Name);
                        il.Emit(OpCodes.Ldarg_1);
                        if (klass.IsValueType)
                            il.Emit(OpCodes.Unbox, klass);
                        else il.Emit(OpCodes.Castclass, klass);

                        Type returnType = null;
                        var targetGetMethod = klass.GetProperty(p.Name).GetGetMethod();
                        var opCode = klass.IsValueType ? OpCodes.Call : OpCodes.Callvirt;
                        il.Emit(opCode, targetGetMethod);
                        returnType = targetGetMethod.ReturnType;

                        if (returnType.IsValueType)
                        {
                            il.Emit(OpCodes.Box, returnType);
                        }
                        il.Emit(OpCodes.Call, formatterForObject);
                    }
                    else
                    {
                        il.Emit(OpCodes.Ldstr, p.Name);
                        string html = ((HtmlAsAttribute)attr).Html;

                        il.Emit(OpCodes.Ldarg_1);
                        if (klass.IsValueType)
                            il.Emit(OpCodes.Unbox, klass);
                        else il.Emit(OpCodes.Castclass, klass);

                        Type returnType = null;
                        var targetGetMethod = klass.GetProperty(p.Name).GetGetMethod();
                        var opCode = klass.IsValueType ? OpCodes.Call : OpCodes.Callvirt;
                        il.Emit(opCode, targetGetMethod);
                        returnType = targetGetMethod.ReturnType;

                        if (returnType.IsValueType)
                        {
                            il.Emit(OpCodes.Box, returnType);
                        }
                        il.Emit(OpCodes.Ldstr, html);
                        il.Emit(OpCodes.Call, formatterForAttr);
                    }
                    il.Emit(OpCodes.Call, concat);
                }
            }
            else
            {
                LocalBuilder target = il.DeclareLocal(klass);
                il.Emit(OpCodes.Ldarg_1);          // push target
                il.Emit(OpCodes.Castclass, klass); // castclass
                il.Emit(OpCodes.Stloc, target);    // store on local variable 

                il.Emit(OpCodes.Ldstr, "");
                il.Emit(OpCodes.Ldloc_0);
                il.Emit(OpCodes.Call, formatterForArray);
                il.Emit(OpCodes.Call, concat);
            }
            il.Emit(OpCodes.Ret);              // ret

            //check if dll already exits

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

            string table = "<table class='table table-hover'>{0}</table>";
            return String.Format(table, ObjFieldsToString(arr));
        }
    }
}
