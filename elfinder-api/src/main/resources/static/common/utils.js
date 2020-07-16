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
    'text/x-web-markdown'     : 'xml'
};