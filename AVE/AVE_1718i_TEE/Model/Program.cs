﻿namespace Model
{
    class Stock {
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
        [NoEffects]
        public double BuildInterest(Portfolio port, Store st) { ... }
    }

    class Program
    {
        static void Main(string[] args)
        {
        }
    }
}
