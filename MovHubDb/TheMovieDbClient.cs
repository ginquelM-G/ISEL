using MovHubDb.Model;
using Newtonsoft.Json;
using System;
using System.Net;

namespace MovHubDb
{
    public class TheMovieDbClient
    {
        private readonly string API_KEY = "b531994cdfaa8e3b441e4086b1c6756d";
        private readonly WebClient client = new WebClient() { Encoding = System.Text.Encoding.UTF8 };

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/search/movie?api_key=*****&query=war%20games
        /// </summary>
        public MovieSearchItem[] Search(string title, int page)
        {
            string path = "https://api.themoviedb.org/3/search/movie?api_key={0}&query={1}&page={2}";
            try
            {
                string body = client.DownloadString(String.Format(path, API_KEY, title, page));
                MovieSearch search = (MovieSearch) JsonConvert.DeserializeObject(body, typeof(MovieSearch));
                return search.Results;
            } catch(WebException ex)
            {
                return new MovieSearchItem[0];
            }
            
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/movie/508?api_key=*****
        /// </summary>
        public Movie MovieDetails(int id) {
            string path = "https://api.themoviedb.org/3/movie/{0}?api_key={1}";
            try
            {
                string body = client.DownloadString(String.Format(path, id, API_KEY));
                Movie search = (Movie)JsonConvert.DeserializeObject(body, typeof(Movie));
                return search;
            } catch(WebException ex)
            {
                return new Movie();
            }
            
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/movie/508/credits?api_key=*****
        /// </summary>
        public CreditsItem[] MovieCredits(int id) {
            string path = "https://api.themoviedb.org/3/movie/{0}/credits?api_key={1}";
            try
            {
                string body = client.DownloadString(String.Format(path, id, API_KEY));
                Credits search = (Credits)JsonConvert.DeserializeObject(body, typeof(Credits));
                return search.Cast;
            } catch (WebException)
            {
                return new CreditsItem[0];
            }

        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/person/3489?api_key=*****
        /// </summary>
        public Person PersonDetais(int actorId)
        {
            string path = "https://api.themoviedb.org/3/person/{0}?api_key={1}";
            try
            {
                string body = client.DownloadString(String.Format(path, actorId, API_KEY));
                Person p = (Person)JsonConvert.DeserializeObject(body, typeof(Person));
                p.Profile_Path = "https://image.tmdb.org/t/p/original" + p.Profile_Path;
                return p;
            } catch(WebException ex)
            {
                return new Person();
            }

        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/person/3489/movie_credits?api_key=*****
        /// </summary>
        public MovieSearchItem[] PersonMovies(int actorId) {
            string path = "https://api.themoviedb.org/3/person/{0}/movie_credits?api_key={1}";
            try
            {
                string body = client.DownloadString(String.Format(path, actorId, API_KEY));
                PersonCredits pc = (PersonCredits)JsonConvert.DeserializeObject(body, typeof(PersonCredits));
                return pc.Cast;
            } catch(WebException ex)
            {
                return new MovieSearchItem[0];
            }

        }
    }
}
