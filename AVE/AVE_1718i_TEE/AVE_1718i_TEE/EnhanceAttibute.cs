using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AVE_1718i_TEE
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
    }
}
