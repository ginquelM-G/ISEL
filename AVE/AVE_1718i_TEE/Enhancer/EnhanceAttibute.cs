using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;

namespace Enhancer
{
    [AttributeUsage(AttributeTargets.Property | AttributeTargets.Method)]
    public abstract class EnhanceAttribute : Attribute
    {
        public abstract void Check(object[] args);
    }


    public class Enhancer
    {

        public static T Build<T>(params object[] args)
        {
            Type t = typeof(T);
            T res = (T)Activator.CreateInstance(t, args);
            return res;
        }


        public static void Main()
        {

            Type t = typeof(Stock);
            string newType = t.Name;

            AssemblyName aName = new AssemblyName("DynamicAssembly");
            AssemblyBuilder ab =
                AppDomain.CurrentDomain.DefineDynamicAssembly(aName, AssemblyBuilderAccess.RunAndSave);

            // For a single-module assembly, the module name is usually
            // the assembly name plus an extension.
            ModuleBuilder mb = ab.DefineDynamicModule(aName.Name, aName.Name + ".dll");

            TypeBuilder tb = mb.DefineType (
                newType,
                TypeAttributes.Public
            );

            //// Add a private field of type int (Int32).
            //FieldBuilder fbNumber = tb.DefineField(
            //    "m_number",
            //    typeof(int),
            //    FieldAttributes.Private);

            //Define a onstructor that takes an object arguments and
            // stores it in 
            Type[] parameterTypes = { typeof(object[]) };
            ConstructorBuilder ctor1 = tb.DefineConstructor(
                MethodAttributes.Public,
                CallingConventions.Standard,
                parameterTypes);

            ILGenerator ctor1IL = ctor1.GetILGenerator();
            ctor1IL.Emit(OpCodes.Ldarg_0);
            ctor1IL.Emit(OpCodes.Call, typeof(object).GetConstructor(Type.EmptyTypes));

            ctor1IL.Emit(OpCodes.Ldarg_0);
            ctor1IL.Emit(OpCodes.Ret);


            //Finish the type
            Type type = tb.CreateType();



            ab.Save(aName.Name + ".dll");
             



        }
    }


}
