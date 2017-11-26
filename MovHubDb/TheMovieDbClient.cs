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
            string body = client.DownloadString(String.Format(path, API_KEY, title, page));
            MovieSearch search = (MovieSearch) JsonConvert.DeserializeObject(body, typeof(MovieSearch));
            return search.Results;
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/movie/508?api_key=*****
        /// </summary>
        public Movie MovieDetails(int id) 
        {
            string path = "https://api.themoviedb.org/3/movie/{0}?api_key={1}";            
            string body = client.DownloadString(String.Format(path, id, API_KEY));
            Movie search = (Movie)JsonConvert.DeserializeObject(body, typeof(Movie));
            return search;   
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/movie/508/credits?api_key=*****
        /// </summary>
        public CreditsItem[] MovieCredits(int id) 
        {
            string path = "https://api.themoviedb.org/3/movie/{0}/credits?api_key={1}";
            string body = client.DownloadString(String.Format(path, id, API_KEY));
            Credits search = (Credits)JsonConvert.DeserializeObject(body, typeof(Credits));
            return search.Cast;
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/person/3489?api_key=*****
        /// </summary>
        public Person PersonDetais(int actorId)
        {
            string path = "https://api.themoviedb.org/3/person/{0}?api_key={1}";
            string body = client.DownloadString(String.Format(path, actorId, API_KEY));
            Person p = (Person)JsonConvert.DeserializeObject(body, typeof(Person));
            return p;
        }

        /// <summary>
        /// e.g.: https://api.themoviedb.org/3/person/3489/movie_credits?api_key=*****
        /// </summary>
        public MovieSearchItem[] PersonMovies(int actorId) 
        {
            string path = "https://api.themoviedb.org/3/person/{0}/movie_credits?api_key={1}";
            string body = client.DownloadString(String.Format(path, actorId, API_KEY));
            PersonCredits pc = (PersonCredits)JsonConvert.DeserializeObject(body, typeof(PersonCredits));
            return pc.Cast;
        }
    }
}
