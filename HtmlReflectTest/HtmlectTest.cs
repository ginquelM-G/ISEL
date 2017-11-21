using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MovHubDb;
using HtmlReflect;
using MovHubDb.Model;
using System.Net;

namespace HtmlReflectTest
{
    [TestClass]
    public class HtmlectTest
    {
        [TestMethod]
        public void ToHtmlTest()
        {
            Htmlect html = new Htmlect();
            TheMovieDbClient client = new TheMovieDbClient();
            MovieSearchItem res = client.Search("drive", 1)[0];
            string htmlatual=html.ToHtml(res);
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
            Htmlect html = new Htmlect();
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
            Htmlect html = new Htmlect();
            Movie movie = client.MovieDetails(346364);
            Assert.IsTrue(html.ToHtml(movie).Contains("href"));
        }

        [TestMethod]
        public void HtmlIgnoreAttributeTest()
        {
            TheMovieDbClient client = new TheMovieDbClient();
            Htmlect html = new Htmlect();
            Movie movie = client.MovieDetails(346364);
            Assert.IsFalse(html.ToHtml(movie).Contains("budget"));
        }

    }

    [TestClass]
    public class MovHubDbTest
    {
        [TestMethod]
        public void SeachMovieTest()
        {
            TheMovieDbClient client = new TheMovieDbClient();
            MovieSearchItem res = client.Search("drive", 1)[0];
            Assert.AreEqual(64690, res.Id);
            Assert.AreEqual("Drive", res.Title);
            Assert.AreEqual("2011-08-06", res.ReleaseDate);
            Assert.AreEqual(7.5, res.VoteAverage);
        }

        [TestMethod]
        public void MovieTest()
        {
            TheMovieDbClient client = new TheMovieDbClient();
            Movie res = client.MovieDetails(1018);
            Assert.AreEqual("Mulholland Drive", res.OriginalTitle);
            Assert.AreEqual("2001-05-16", res.ReleaseDate);
            Assert.AreEqual(7.7, res.VoteAverage);

        }

        [TestMethod]
        public void CreditsTest()
        {
            TheMovieDbClient client = new TheMovieDbClient();
            CreditsItem res = client.MovieCredits(1018)[2];
            Assert.AreEqual(15008, res.Id);
            Assert.AreEqual("Catherine Lenoix", res.Character);
            Assert.AreEqual("Ann Miller", res.Name);
        }

        [TestMethod]
        public void PersonTest()
        {
            TheMovieDbClient client = new TheMovieDbClient();
            Person res = client.PersonDetais(15008);
            Assert.AreEqual("1923-04-12", res.Birthday);
            Assert.AreEqual("2004-01-22", res.Deathday);
            Assert.AreEqual("Ann Miller", res.Name);
        }

        [TestMethod]
        public void PersonMoviesTest()
        {
            TheMovieDbClient client = new TheMovieDbClient();
            MovieSearchItem res = client.PersonMovies(15008)[1];
            Assert.AreEqual("On the Town", res.Title);
            Assert.AreEqual("1949-12-08", res.ReleaseDate);
            Assert.AreEqual(6.9, res.VoteAverage);
        }

    }

}
