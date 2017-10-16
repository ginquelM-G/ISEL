using HtmlReflect;
using System;

namespace MovHubDb.Model
{
    public class CreditsItem
    {
        [HtmlAs("<a href='/person/{value}/movies'>{value}</a>")]
        public int Id { get; set; }
        public String Character { get; set; }
        public String Name { get; set; }
    }
}