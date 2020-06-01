CKEDITOR.editorConfig = function (config) {
    config.language = 'zh-cn';
    config.resize_enabled = false;
    //config.toolbar = 'MyToolbar';
    config.toolbar_NOToolbar = [['Bold']];
    config.toolbar_Simple = [['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink']];
    config.toolbar_MyToolbar = [
        ['Source', 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript',
            'Superscript', 'TextColor', 'BGColor', '-', 'Cut', 'Copy',
            'Paste', 'Link', 'Unlink'],
        ['Undo', 'Redo', '-', 'JustifyLeft', 'JustifyCenter',
            'JustifyRight', 'JustifyBlock'],
        ['Image', 'Flash', 'Table', 'Smiley', 'SpecialChar', 'PageBreak',
            'Styles', 'Format', 'Font', 'FontSize', 'Maximize',
            'UIColor']];
    config.filebrowserImageBrowseUrl = 'ckUploader?Type=Image';
};