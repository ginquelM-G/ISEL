using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Enhancer
{

    //[AttributeUsage(AttributeTargets.Property | AttributeTargets.Method)]
    //public abstract class EnhanceAttribute : Attribute
    //{
    //    public abstract void Check(object[] args);
    //}


    public class NonNull : EnhanceAttribute
    {
        public override void Check(object[] args)
        {
            if (args == null) throw new Exception("Exception NonNull launch");
            foreach (object o in args)
            {
                if (o == null) throw new Exception("NULL " + o.ToString());
            }
        }
    }


    public class Min : EnhanceAttribute
    {
        int minValue;
        public Min(int minValue)
        {
            this.minValue = minValue;
        }

        public Min(double minValue)
        {
            this.minValue = (int)minValue;
        }

        public override void Check(object[] args)
        {
            foreach(object o in args)
            {
                if ((Double)o < minValue) throw new Exception();
            }
            Console.WriteLine("Min Attribute Check with Success");
        }
    }

    public class Accept : EnhanceAttribute
    {
        object[] argsAcc;
        public Accept(params object[] args)
        {
            this.argsAcc = args;
        }
        public override void Check(object[] args)
        {
            bool equal = false;
            foreach(object o in argsAcc)
            {
                foreach(object oP in args)
                {
                    if (o.Equals(oP)) equal = true;
                }
                if (!equal) throw new Exception("Value not Accept");

            }
        }
    }


    public class Max : EnhanceAttribute
    {
        int maxValue;
        public Max(int maxValue)
        {
            this.maxValue = maxValue;
        }
        public override void Check(object[] args)
        {
            foreach (object o in args)
            {
                if ((Double)o < maxValue) throw new Exception();
            }
            Console.WriteLine("Max Attribute Check with Success");

        }
    }


    public class NoEffects : EnhanceAttribute
    {
        public override void Check(object[] args)
        {
            throw new NotImplementedException();
        }
    }


    class Stock
    {
        public Stock(string name, string index) { }
        [NonNull]
        public virtual string Market { get; set; } // set dará excepção para valores null
        [Min(73)]
        public virtual long Quote { get; set; } // set dará excepção para valores < 73
        [Min(0.325)]
        public virtual double Rate { get; set; } // set dará excepção para valores < 0,325
        [Accept("Jenny", "Lily", "Valery")]
        public virtual string Trader { get; set; } // set só aceita valores Jenny, Lily e Valery
        [Max(58)]
        public virtual int Price { get; set; } // set dará excepção para valores < 58
                                               // dará excepção se o estado de this ou algum dos parâmetros tiver sido alterado
                                               // pela execução do método anotado -- BuildInterest
        //[NoEffects]
        //public double BuildInterest(Portfolio port, Store st) { }
    }



   

    class Program
    {
        static void print(Type t)
        {
            PropertyInfo [] propertyInfo = t.GetProperties();

            foreach (PropertyInfo p in propertyInfo) {
                object[] attrs = p.GetCustomAttributes(false);
                for (int i = 0; i < attrs.Length; ++i)
                {
                    if (attrs[i].GetType() == typeof(NonNull)) {
                        Console.WriteLine(" ## ");
                        NonNull n = (NonNull)attrs[i];
                        n.Check(new object[] { "hell no" });
                    }

                
                    Console.WriteLine(attrs[i].ToString());
                }
            }


        }

        static void Main(string[] args)
        {

            Stock st = Enhancer.Build<Stock>("Apple", "Dow Jones");

            Console.WriteLine(st.ToString());

            print(st.GetType());
            Console.WriteLine(st.Market);
            Console.ReadKey();
        }
    }
}
