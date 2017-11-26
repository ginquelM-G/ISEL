namespace MovHubWebApp
{
    internal class MovHubViewModel
    {
        public string Heading { get; set; }
        public string Body { get; set; }
        public string profilePath { get; set; }

        public MovHubViewModel(string heading, string body)
        {
            this.Heading = heading;
            this.Body = body;
        }


        public MovHubViewModel(string heading, string body, string profilePath)
        {
            this.Heading = heading;
            this.Body = body;
            this.profilePath = profilePath;
        }
    }
}