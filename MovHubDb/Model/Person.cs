using HtmlReflect;
using Newtonsoft.Json;

namespace MovHubDb.Model
{
    public class Person
    {
        public string Name { get; set; }
        public string Birthday { get; set; }
        public string Deathday { get; set; }
        public string Biography { get; set; }
        public string Popularity { get; set; }
        [HtmlIgnore] public string Profile_Path { get; set; }
        [JsonProperty("place_of_birth")]
        public string PlaceOfBirth { get; set; }

    }
}
