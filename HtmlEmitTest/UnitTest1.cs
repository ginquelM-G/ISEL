using System;
using HtmlEmit;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb;
using MovHubDb.Model;

namespace HtmlEmitTest
{
    [TestClass]
    public class UnitTest1
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
    }
}
