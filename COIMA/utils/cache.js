//module.exports = MemoryCache
module.exports = {
    MemoryCacheInit, add, checkIfURIisInTheCache, get
    //cacheOperation
    //add
    //,myCache
} 



var myCache=null || new Map()


function MemoryCacheInit(){
    myCache = new Map()
}

function MemoryCache(){
    if(this.myCache == null)
        this.myCache = myCache; //new Map()

    this.cacheOperation = cacheOperation
    this.tryToGetInTheCache = tryToGetInTheCache
    /**    
    this.add = function add(key, value){
        this.myCache.set(key, value)   
        //console.log('add => ' + this.myCache.get(key))
        return;
    }
    */

    console.log('##### MemoryCache()  '+ myCache)
}



function add(key, value){ 
    if (myCache != null && myCache != null){
        myCache.set(key, value)
        console.log('\nCache Log:')       
        myCache.forEach(function(value, key){
            console.log('key='+key)
        });
    }else{ 
        console.log('cache == null ' + this.myCache)
    }      
}

function cacheOperation(uri, data){
   /*  if (cache != null && cache.myCache != null){
        cache.add(uri, data)
        console.log('\nCache Log:\n')       
        cache.myCache.forEach(function(value, key){
            console.log('key='+key)
        }); */

     if (myCache != null && myCache != null){
        add(uri, data)
        console.log('\nCache Log:')       
        myCache.forEach(function(value, key){
            console.log('key='+key)
        });
    }else{ 
        console.log('cache == null ' + this.myCache)
    }               
}

function checkIfURIisInTheCache(key){
    return myCache.has(key)
}

function get(uri){
    return myCache.get(uri)
}
