using System;
using HtmlEmit;
using HtmlReflect;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb.Model;
using MovHubDb;

namespace Benchmarking
{
    [TestClass]
    public class UnitTest1
    {
        static readonly int SIZE = 200;
        static MovieSearchItem[] moviesApi = new TheMovieDbClient().Search("it", 1);
        static MovieSearchItem[] movies = new MovieSearchItem[SIZE];

        [TestMethod]
        public void TestNumOpPerSec()
        {
            populate(ref movies);
            long opEmit = NBench.Bench(BenchEmit, "Emit");
            long opReflect = NBench.Bench(BenchReflect, "Reflect");
            Console.WriteLine("Emit: {0}\nReflect: {1}", opEmit, opReflect);
            Assert.IsTrue(opEmit > opReflect);
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

        private void populate(ref MovieSearchItem[] movies)
        {
            for (int i = 0; i < SIZE; i++) {
                movies[i] = new MovieSearchItem { Id = i + 100, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7 };
            }
        }
    }
}
