(function () {
var hr = (function () {
  'use strict';

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager');

  var register = function (editor) {
    editor.addCommand('InsertHorizontalRule', function () {
      editor.execCommand('mceInsertContent', false, '<hr />');
    });
  };
  var $_7go9x1cmjn6umkaz = { register: register };

  var register$1 = function (editor) {
    editor.addButton('hr', {
      icon: 'hr',
      tooltip: 'Horizontal line',
      cmd: 'InsertHorizontalRule'
    });
    editor.addMenuItem('hr', {
      icon: 'hr',
      text: 'Horizontal line',
      cmd: 'InsertHorizontalRule',
      context: 'insert'
    });
  };
  var $_2j7qqzcnjn6umkb0 = { register: register$1 };

  global.add('hr', function (editor) {
    $_7go9x1cmjn6umkaz.register(editor);
    $_2j7qqzcnjn6umkb0.register(editor);
  });
  function Plugin () {
  }

  return Plugin;

}());
})();
