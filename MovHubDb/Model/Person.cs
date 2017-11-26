using Newtonsoft.Json;
using HtmlReflect;
namespace MovHubDb.Model
{
    public class Person
    {
        public string Name { get; set; }
        public string Birthday { get; set; }
        public string Deathday { get; set; }
        public string Biography { get; set; }
        public string Popularity { get; set; }
        [JsonProperty("place_of_birth")]
        public string PlaceOfBirth { get; set; }
        [HtmlAs("<div style = 'position:absolute; top:0; right:0;'><img width = '50%' src = 'http://image.tmdb.org/t/p/w185/{value}\'></div>")]
        [JsonProperty("profile_path")]
        public string Photo { get; set; }

    }
}
