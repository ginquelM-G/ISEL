using HtmlReflect;
using Newtonsoft.Json;
using System;

namespace MovHubDb.Model
{
    public class MovieSearchItem
    {
        [HtmlAs("<a href='/movies/{value}'>{value}</a>")]
        public int Id { get; set; }
        public String Title { get; set; }
        [JsonProperty("release_date")]
        public String ReleaseDate { get; set; }
        [JsonProperty("vote_average")]
        public double VoteAverage { get; set; }
    }
}