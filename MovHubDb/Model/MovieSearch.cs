using HtmlReflect;
using System;

namespace MovHubDb.Model
{
    public class MovieSearch
    {
        [HtmlIgnore] public int Page { get; set; }
        public MovieSearchItem[] Results;
    }
}