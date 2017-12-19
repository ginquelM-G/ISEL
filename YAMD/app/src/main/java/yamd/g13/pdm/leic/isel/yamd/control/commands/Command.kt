package yamd.g13.pdm.leic.isel.yamd.control.commands

import yamd.g13.pdm.leic.isel.yamd.control.Endpoint
import yamd.g13.pdm.leic.isel.yamd.control.HttpRequests

/**
 * Created by tony_mendes on 01/11/2017.
 */
abstract class Command {

    abstract fun<T> parseResult(jsonResult: String): List<T>
    abstract fun getUri(endpoint: Endpoint) : String
    companion object {

        private val commands = HashMap<Endpoint, Command>()
        private val provider = HttpRequests()
        private var initialized = false
        private val recentPaths = ArrayList<String>()

        private fun initCommands() {
            commands.put(Endpoint.POPULAR, GetMovies())
            commands.put(Endpoint.NOW_PLAYING, GetMovies())
            commands.put(Endpoint.SEARCH, GetMovies())
            commands.put(Endpoint.UPCOMING, GetMovies())
            commands.put(Endpoint.MOVIE_DETAIL, GetMovieDetails())
            initialized = true
        }

        fun<T> execute(endpoint: Endpoint): List<T> {
            if (!initialized){
                initCommands()
            }
            if(!commands.containsKey(endpoint))
                return ArrayList()
            var command = commands[endpoint]!!
            var url = command.getUri(endpoint) + endpoint.query + endpoint.nextPage()
            var currentUrl = command.getUri(endpoint) + endpoint.query + endpoint.getCurrentPage()
            if(recentPaths.contains(currentUrl))
                return command.parseResult("")
            recentPaths.add(currentUrl)
            return command.parseResult(provider.get(url)[0])
        }
    }
}