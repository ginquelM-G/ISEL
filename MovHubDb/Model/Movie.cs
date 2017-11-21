using HtmlReflect;
using Newtonsoft.Json;
using System;

namespace MovHubDb.Model
{
    public struct Movie
    {
        [HtmlIgnore] public int Id { get; set; }
        [JsonProperty("original_title")]
        public string OriginalTitle { get; set; }
        [HtmlAs("<li class='list-group-item'><a href='/movies/{value}/credits'>Cast and crew </a></li>")]
        //public string Tagline { get; set; }
        public string Credits { get { return Id.ToString(); } }
        [HtmlIgnore] public long Budget { get; set; }
        public double Popularity { get; set; }
        [JsonProperty("vote_average")]
        public double VoteAverage { get; set; }
        [JsonProperty("release_date")]
        public string ReleaseDate { get; set; }
        [HtmlAs("<div class='card-body bg-light'><div><strong>{name}</strong>:</div>{value}</div>")]
        public String Overview { get; set; }

    }
}