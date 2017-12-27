using System;
using HtmlEmit;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb;
using MovHubDb.Model;
using System.Collections.Generic;
using System.Linq;


namespace HtmlEmitTest
{
    [TestClass]
    public class UnitTest2
    {
        [TestMethod]
        public void ForTypeDetailsTest()
        {
            Emitter emitter = new Emitter();
            emitter.ForTypeDetails<Movie>(st =>
                "<p>" + "<strong>" + st.OriginalTitle + "</strong> (" + st.Overview + ")" + "</p>");
            TheMovieDbClient client = new TheMovieDbClient();
            Movie movie = client.MovieDetails(346364);
            string htmlatual = emitter.ToHtml(movie);
            string expected = "<p><strong>It</strong> " +
                "(In a small town in Maine, seven children known as The Losers Club come face " +
                "to face with life problems, bullies and a monster that takes the shape of a clown " +
                "called Pennywise.)</p>";
            Assert.AreEqual(expected, htmlatual);
        }

        [TestMethod]
        public void ForTypeInTableTest()
        {
            Emitter html = new Emitter();
            IEnumerable<string> headers = new string[] { "Id", "Title", "ReleaseDate" };
            html.ForTypeInTable<MovieSearchItem>(headers, mov =>
            {
                const string template = "<tr><td>{0}</td><td>{1}</td><td>{2}</td></tr>";
                return String.Format(template, mov.Id, mov.Title, mov.ReleaseDate);
            });

            TheMovieDbClient client = new TheMovieDbClient();
            IEnumerable<MovieSearchItem> res = client.Search("drive", 1);
            MovieSearchItem item = res.ElementAt(0);
            string htmlatual = html.ToHtml<MovieSearchItem>(new List<MovieSearchItem> { item });
            string expected = "<table class='table table-hover'>" +
                "<thead>" +
                "<tr><th>Id</th><th>Title</th><th>ReleaseDate</th></tr>" +
                "</thead>" +
                "<tbody>" +
                "<tr><td>64690</td><td>Drive</td><td>2011-08-06</td></tr>" +
                "</tbody></table>";
            Assert.AreEqual(expected, htmlatual);
        }

        [TestMethod]
        public void ForSequenceOfTest()
        {
            Emitter html = new Emitter();
            html.ForSequenceOf<MovieSearchItem>(movs =>
            {
                string liIds = movs.Aggregate("", (prev, mov) => prev + "<li>" + mov.Id + "</li>");
                return "<h1>Movie Ids</h1><ul>" + liIds + "</ul>";
            });

            TheMovieDbClient client = new TheMovieDbClient();
            MovieSearchItem[] res = client.Search("drive", 1);
            string htmlatual = html.ToHtml<MovieSearchItem>(res.Take(2));
            string expected = "<h1>Movie Ids</h1><ul><li>64690</li><li>1018</li></ul>";
            Assert.AreEqual(expected, htmlatual);
        }
    }
}
