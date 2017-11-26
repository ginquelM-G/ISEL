using System;
using HtmlEmit;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb;
using MovHubDb.Model;

namespace HtmlEmitTest
{
    [TestClass]
    public class EmitterTest
    {
        [TestMethod]
        public void ToHtmlTest()
        {
            Emitter emitter = new Emitter();
            TheMovieDbClient client = new TheMovieDbClient();
            MovieSearchItem res = client.Search("drive", 1)[0];
            string htmlatual = emitter.ToHtml(res);
            string expected = "<ul class='list-group'>" +
                "<a href='/movies/64690'>64690</a>" +
                "<li class='list-group-item'>" +
                "<strong>Title</strong>: Drive</li>" +
                "<li class='list-group-item'>" +
                "<strong>ReleaseDate</strong>: 2011-08-06</li>" +
                "<li class='list-group-item'>" +
                "<strong>VoteAverage</strong>: 7,5</li></ul>";
            Assert.AreEqual(expected, htmlatual);
        }

        [TestMethod]
        public void ToHtmlArrayTest()
        {
            Emitter html = new Emitter();
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

        [TestMethod]
        public void HtmlAsAttributeTest()
        {
            TheMovieDbClient client = new TheMovieDbClient();
            Emitter html = new Emitter();
            Movie movie = client.MovieDetails(346364);
            Assert.IsTrue(html.ToHtml(movie).Contains("href"));
        }

        [TestMethod]
        public void HtmlIgnoreAttributeTest()
        {
            TheMovieDbClient client = new TheMovieDbClient();
            Emitter html = new Emitter();
            Movie movie = client.MovieDetails(346364);
            Assert.IsFalse(html.ToHtml(movie).Contains("budget"));
        }
    }
}
