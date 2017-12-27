using Benchmarking;
using HtmlEmit;
using HtmlReflect;
using MovHubDb;
using MovHubDb.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Benchmarking
{
    class Program
    {
        static readonly int SIZE = 200;
        static MovieSearchItem[] moviesApi = new TheMovieDbClient().Search("it", 1);
        static MovieSearchItem[] movies = new MovieSearchItem[SIZE];

        static void Main(string[] args)
        {
            populate(ref movies);

            NBench.Bench(Program.BenchReflect, "Reflect");
            NBench.Bench(Program.BenchEmit, "Emit");
            Console.ReadKey();
        }

        public static void BenchEmit()
        {
            Emitter emit = new Emitter();
            string htmlEmit = emit.ToHtml(movies);
        }

        public static void BenchReflect()
        {
            Htmlect reflect = new Htmlect();
            string htmlReflect = reflect.ToHtml(movies);
        }

        private static void populate(ref MovieSearchItem[] movies)
        {
            for (int i = 0; i < SIZE; i++)
            {
                movies[i] = new MovieSearchItem { Id = i + 100, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7 };
            }
        }
    }
}
