(function () {
var directionality = (function () {
  'use strict';

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager');

  var global$1 = tinymce.util.Tools.resolve('tinymce.util.Tools');

  var setDir = function (editor, dir) {
    var dom = editor.dom;
    var curDir;
    var blocks = editor.selection.getSelectedBlocks();
    if (blocks.length) {
      curDir = dom.getAttrib(blocks[0], 'dir');
      global$1.each(blocks, function (block) {
        if (!dom.getParent(block.parentNode, '*[dir="' + dir + '"]', dom.getRoot())) {
          dom.setAttrib(block, 'dir', curDir !== dir ? dir : null);
        }
      });
      editor.nodeChanged();
    }
  };
  var $_ckhvldb3jn6umk49 = { setDir: setDir };

  var register = function (editor) {
    editor.addCommand('mceDirectionLTR', function () {
      $_ckhvldb3jn6umk49.setDir(editor, 'ltr');
    });
    editor.addCommand('mceDirectionRTL', function () {
      $_ckhvldb3jn6umk49.setDir(editor, 'rtl');
    });
  };
  var $_1mdqqeb2jn6umk48 = { register: register };

  var generateSelector = function (dir) {
    var selector = [];
    global$1.each('h1 h2 h3 h4 h5 h6 div p'.split(' '), function (name) {
      selector.push(name + '[dir=' + dir + ']');
    });
    return selector.join(',');
  };
  var register$1 = function (editor) {
    editor.addButton('ltr', {
      title: 'Left to right',
      cmd: 'mceDirectionLTR',
      stateSelector: generateSelector('ltr')
    });
    editor.addButton('rtl', {
      title: 'Right to left',
      cmd: 'mceDirectionRTL',
      stateSelector: generateSelector('rtl')
    });
  };
  var $_5maw4yb5jn6umk4c = { register: register$1 };

  global.add('directionality', function (editor) {
    $_1mdqqeb2jn6umk48.register(editor);
    $_5maw4yb5jn6umk4c.register(editor);
  });
  function Plugin () {
  }

  return Plugin;

}());
})();
