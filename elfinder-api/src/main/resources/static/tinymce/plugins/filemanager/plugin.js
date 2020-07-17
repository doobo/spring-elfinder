(function () {
  'use strict';

  var imgExt = new Array(".png",".jpg",".jpeg",".bmp",".gif");//图片文件的后缀名
  var docExt = new Array(".doc",".docx");//word文件的后缀名
  var xlsExt = new Array(".xls",".xlsx");//excel文件的后缀名
  var cssExt = new Array(".css");//css文件的后缀名
  var jsExt = new Array(".js");//js文件的后缀名

//获取文件名后缀名
  String.prototype.extension = function(){
    var ext = null;
    var name = this.toLowerCase();
    var i = name.lastIndexOf(".");
    if(i > -1){
      var ext = name.substring(i);
    }
    return ext;
  }

//判断Array中是否包含某个值
  Array.prototype.contain = function(obj){
    for(var i=0; i<this.length; i++){
      if(this[i] === obj)
        return true;
    }
    return false;
  };

  String.prototype.extMatch = function(extType){
    if(extType.contain(this.extension()))
      return true;
    else
      return false;
  }

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager');

  var register = function (editor) {
    editor.addCommand('OpenFileManager', function () {

      editor.windowManager.openUrl({
        url: "./link.html",
        title: '文件管理器',
        width: 1100,
        height: 600
      });

      var listener = function (event) {
        var data = event.data;
        if(data instanceof Array){
          data.forEach((file, index, array) => {
            var name = file.name;
            var url = file.url;

            //判断是否图片文件
            if(name.extMatch(imgExt)){
              editor.execCommand('mceInsertContent', false, '<img src="'+ url +'">');
            } else {
              editor.execCommand('mceInsertContent', false, '<a href="'+ url +'" target="_blank" style="margin:5px;">'+ name +'</a>');
            }

          });
        }
        window.removeEventListener('message', listener);
        tinyMCE.activeEditor.windowManager.close();
      };
      window.addEventListener('message', listener);

    });
  };
  var Commands = { register: register };

  var register$1 = function (editor) {
    editor.ui.registry.addButton('filemanager', {
      icon: 'horizontal-rule',
      tooltip: 'Horizontal line',
      onAction: function () {
        return editor.execCommand('OpenFileManager');
      }
    });
    editor.ui.registry.addMenuItem('filemanager', {
      icon: 'horizontal-rule',
      text: 'Horizontal line',
      onAction: function () {
        return editor.execCommand('OpenFileManager');
      }
    });
  };
  var Buttons = { register: register$1 };

  function Plugin () {
    global.add('filemanager', function (editor) {
      Commands.register(editor);
      Buttons.register(editor);
    });
  }

  Plugin();

}());