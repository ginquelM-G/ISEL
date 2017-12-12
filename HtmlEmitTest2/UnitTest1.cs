using System;
using HtmlEmit;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb;
using MovHubDb.Model;
using System.Collections.Generic;

namespace HtmlEmitTest2
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void ToHtmlTest()
        {
            Emitter emitter = new Emitter();
            emitter.ForTypeDetails<Movie>(st =>
                "<p>" +  "<strong>" + st.OriginalTitle + "</strong> (" + st.Overview + ")" + "</p>");
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
        public void ToHtmlArrayTest()
        {
            Emitter html = new Emitter();
            IEnumerable<string> headers = new string[] { "Id", "Title", "ReleaseDate", "VoteAverage" };
            html.ForTypeInTable<MovieSearchItem>(headers, mov =>
            {
                const string template = "<tr><td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td></tr>";
                return String.Format(template, mov.Id, mov.Title, mov.ReleaseDate, mov.VoteAverage);
            });
            TheMovieDbClient client = new TheMovieDbClient();
            MovieSearchItem res = client.Search("drive", 1)[0];
            string htmlatual = html.ToHtml(new MovieSearchItem[] { res });
            string expected = "<table class='table table-hover'>" +
                "<thead>" +
                "<tr><th>Id</th><th>Title</th><th>ReleaseDate</th><th>VoteAverage</th></tr>" +
                "</thead>" +
                "<tbody>" +
                "<tr><td><a href='/movies/64690'>64690</a></td><td>Drive</td><td>2011-08-06</td><td>7,5</td></tr>" +
                "</tbody></table>";
            Assert.AreEqual(expected, htmlatual);
        }

    }
}
