(function () {
var code = (function () {
  'use strict';

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager');

  var global$1 = tinymce.util.Tools.resolve('tinymce.dom.DOMUtils');

  var getMinWidth = function (editor) {
    return editor.getParam('code_dialog_width', 600);
  };
  var getMinHeight = function (editor) {
    return editor.getParam('code_dialog_height', Math.min(global$1.DOM.getViewPort().h - 200, 500));
  };
  var $_nus1ma1jn6umjyw = {
    getMinWidth: getMinWidth,
    getMinHeight: getMinHeight
  };

  var setContent = function (editor, html) {
    editor.focus();
    editor.undoManager.transact(function () {
      editor.setContent(html);
    });
    editor.selection.setCursorLocation();
    editor.nodeChanged();
  };
  var getContent = function (editor) {
    return editor.getContent({ source_view: true });
  };
  var $_1k8ts6a3jn6umjyz = {
    setContent: setContent,
    getContent: getContent
  };

  var open = function (editor) {
    var minWidth = $_nus1ma1jn6umjyw.getMinWidth(editor);
    var minHeight = $_nus1ma1jn6umjyw.getMinHeight(editor);
    var win = editor.windowManager.open({
      title: 'Source code',
      body: {
        type: 'textbox',
        name: 'code',
        multiline: true,
        minWidth: minWidth,
        minHeight: minHeight,
        spellcheck: false,
        style: 'direction: ltr; text-align: left'
      },
      onSubmit: function (e) {
        $_1k8ts6a3jn6umjyz.setContent(editor, e.data.code);
      }
    });
    win.find('#code').value($_1k8ts6a3jn6umjyz.getContent(editor));
  };
  var $_80io45a0jn6umjyv = { open: open };

  var register = function (editor) {
    editor.addCommand('mceCodeEditor', function () {
      $_80io45a0jn6umjyv.open(editor);
    });
  };
  var $_70uz1u9zjn6umjyt = { register: register };

  var register$1 = function (editor) {
    editor.addButton('code', {
      icon: 'code',
      tooltip: 'Source code',
      onclick: function () {
        $_80io45a0jn6umjyv.open(editor);
      }
    });
    editor.addMenuItem('code', {
      icon: 'code',
      text: 'Source code',
      onclick: function () {
        $_80io45a0jn6umjyv.open(editor);
      }
    });
  };
  var $_4q43kya4jn6umjz0 = { register: register$1 };

  global.add('code', function (editor) {
    $_70uz1u9zjn6umjyt.register(editor);
    $_4q43kya4jn6umjz0.register(editor);
    return {};
  });
  function Plugin () {
  }

  return Plugin;

}());
})();
