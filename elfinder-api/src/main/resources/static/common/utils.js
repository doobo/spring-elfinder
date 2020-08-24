function queryUrlParam(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null) return  unescape(r[2]); 
    return null;
}

var mimeMode = {
    'text/x-php'              : 'php',
    'application/x-php'       : 'php',
    'text/html'               : 'html',
    'application/xhtml+xml'   : 'html',
    'text/javascript'         : 'javascript',
    'application/javascript'  : 'javascript',
    'text/x-matlab'           : 'javascript',
    'text/css'                : 'css',
    'text/x-c'                : 'c_cpp',
    'text/x-csrc'             : 'c_cpp',
    'text/x-chdr'             : 'c_cpp',
    'text/x-c++'              : 'c_cpp',
    'text/x-c++src'           : 'c_cpp',
    'text/x-c++hdr'           : 'c_cpp',
    'text/x-shellscript'      : 'sh',
    'application/x-csh'       : 'sh',
    'text/x-python'           : 'python',
    'text/x-java'             : 'java',
    'text/x-java-source'      : 'java',
    'text/x-ruby'             : 'ruby',
    'text/x-perl'             : 'perl',
    'application/x-perl'      : 'perl',
    'text/x-sql'              : 'sql',
    'text/xml'                : 'xml',
    'application/docbook+xml' : 'xml',
    'application/xml'         : 'xml',
    'text/plain'              : 'xml',
    'application/json'        : 'xml',
    'text/x-ini'              : 'xml',
    'application/x-sh'        : 'xml',
    'application/x-bat'       : 'xml',
    'text/x-web-markdown'     : 'xml'
};
(function(window){
    var LS, noop = function(){}, document = window.document, notSupport = {set:noop,get:noop,remove:noop,clear:noop,each:noop,obj:noop,length:0};
    (function(){
        if( "localStorage" in window ){
            try{
                LS = window.localStorage;
                return;
            }catch(e){
            }
        }

        var o = document.getElementsByTagName("head")[0], hostKey = window.location.hostname || "localStorage", d = new Date(), doc, agent;
        if(!o.addBehavior){
            try{
                LS = window.localStorage;
            }catch(e){
                LS = null;
            }
            return;
        }

        try{
            agent = new ActiveXObject('htmlfile');
            agent.open();
            agent.write('<s' + 'cript>document.w=window;</s' + 'cript><iframe src="/favicon.ico"></iframe>');
            agent.close();
            doc = agent.w.frames[0].document;
            //这里通过代理document创建head，可以使存储数据垮文件夹访问
            o = doc.createElement('head');
            doc.appendChild(o);
        }catch(e){
            o = document.getElementsByTagName("head")[0];
        }

        //初始化userData
        try{
            d.setDate(d.getDate() + 36500);
            o.addBehavior("#default#userData");
            o.expires = d.toUTCString();
            o.load(hostKey);
            o.save(hostKey);
        }catch(e){
            return;
        }
        var root, attrs;
        try{
            root = o.XMLDocument.documentElement;
            attrs = root.attributes;
        }catch(e){
            return;
        }
        var prefix = "p__hack_", spfix = "m-_-c",
            reg1 = new RegExp("^"+prefix),
            reg2 = new RegExp(spfix,"g"),
            encode = function(key){ return encodeURIComponent(prefix + key).replace(/%/g, spfix); },
            decode = function(key){ return decodeURIComponent(key.replace(reg2, "%")).replace(reg1,""); };
        LS= {
            length: attrs.length,
            isVirtualObject: true,
            getItem: function(key){
                //IE9中 通过o.getAttribute(name);取不到值，所以才用了下面比较复杂的方法。
                return (attrs.getNamedItem( encode(key) ) || {nodeValue: null}).nodeValue||root.getAttribute(encode(key));
            },
            setItem: function(key, value){
                //IE9中无法通过 o.setAttribute(name, value); 设置#userData值，而用下面的方法却可以。
                try{
                    root.setAttribute( encode(key), value);
                    o.save(hostKey);
                    this.length = attrs.length;
                }catch(e){//这里IE9经常报没权限错误,但是不影响数据存储
                }
            },
            removeItem: function(key){
                //IE9中无法通过 o.removeAttribute(name); 删除#userData值，而用下面的方法却可以。
                try{
                    root.removeAttribute( encode(key) );
                    o.save(hostKey);
                    this.length = attrs.length;
                }catch(e){//这里IE9经常报没权限错误,但是不影响数据存储
                }
            },
            clear: function(){
                while(attrs.length){
                    this.removeItem( attrs[0].nodeName );
                }
                this.length = 0;
            },
            key: function(i){
                return attrs[i] ? decode(attrs[i].nodeName) : undefined;
            }
        };
        if( !("localStorage" in window) )
            window.localStorage = LS;
    })();

    window.LS = !LS ? notSupport : {
        set : function(key, value){
            //fixed iPhone/iPad 'QUOTA_EXCEEDED_ERR' bug
            if( this.get(key) !== undefined )
                this.remove(key);
            LS.setItem(key, value);
            this.length = LS.length;
        },
        get : function(key){
            var v = LS.getItem(key);
            return v === null ? undefined : v;
        },
        remove : function(key){ LS.removeItem(key);this.length = LS.length; },
        clear : function(){ LS.clear();this.length = 0; },
        each : function(callback){
            var list = this.obj(), fn = callback || function(){}, key;
            for(key in list)
                if( fn.call(this, key, this.get(key)) === false )
                    break;
        },
        obj : function(){
            var list={}, i=0, n, key;
            if( LS.isVirtualObject ){
                list = LS.key(-1);
            }else{
                n = LS.length;
                for(; i<n; i++){
                    key = LS.key(i);
                    list[key] = this.get(key);
                }
            }
            return list;
        },
        length : LS.length
    };
    if( window.jQuery ) window.jQuery.LS = window.LS;
})(window);

(function(){
    $.ajaxSetup({
        beforeSend: function(jqXHR, settings) {
            try {
                if (LS.get("authorization")) {
                    jqXHR.setRequestHeader('authorization',LS.get("authorization"));
                }
                if (LS.get("Authorization")) {
                    jqXHR.setRequestHeader('Authorization',LS.get("Authorization"));
                }
                if (LS.get("token")) {
                    jqXHR.setRequestHeader('token',LS.get("token"));
                }
                if (LS.get("Token")) {
                    jqXHR.setRequestHeader('Token',LS.get("Token"));
                }
                if(settings.url.endsWith("]")){
                    settings.url = settings.url.substring(0, settings.url.lastIndexOf("&"));
                }
            } catch (e) {
            }
        }
    });
})();