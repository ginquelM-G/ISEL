using System;
using HtmlEmit;
using HtmlReflect;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb.Model;

namespace Benchmarking
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void TestMethod1()
        {
            long timeEmit = NBench.Bench(BenchEmit, "Emit");
            long timeReflect = NBench.Bench(BenchReflect, "Reflect");
            Console.WriteLine("Emit: {0}\n Reflect: {1}", timeEmit, timeReflect);
            Assert.IsTrue(timeEmit < timeReflect);
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

        static MovieSearchItem[] movies = new MovieSearchItem[] {
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7},
            new MovieSearchItem { Id = 1018, Title = "Mulholland Drive", ReleaseDate = "2001-05-16", VoteAverage = 7.7}
        };
    }
}
