import React, { useRef, useEffect } from 'react';
import Editor from '@monaco-editor/react';
import { useProject } from '../../context/ProjectContext';
import { useCompiler } from '../../context/CompilerContext';

export const MonacoCodeEditor = () => {
  const { activeFile, updateActiveFileContent, saveFile, autosaveEnabled } = useProject();
  const { editorMarkers, runCompilation } = useCompiler();
  const editorRef = useRef(null);
  const monacoRef = useRef(null);

  const handleEditorDidMount = (editor, monaco) => {
    editorRef.current = editor;
    monacoRef.current = monaco;

    // Configure editor shortcuts
    editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.Enter, () => {
      runCompilation('ALL');
    });

    editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyS, () => {
      if (activeFile) {
        saveFile(activeFile.id);
      }
    });

    // Custom dark theme tweaks
    monaco.editor.defineTheme('cloudCompilerDark', {
      base: 'vs-dark',
      inherit: true,
      rules: [
        { token: 'comment', foreground: '8b949e', fontStyle: 'italic' },
        { token: 'keyword', foreground: 'ff7b72', fontStyle: 'bold' },
        { token: 'identifier', foreground: '79c0ff' },
        { token: 'number', foreground: '79c0ff' },
        { token: 'string', foreground: 'a5d6ff' },
        { token: 'delimiter', foreground: 'c9d1d9' },
        { token: 'operator', foreground: 'ff7b72' }
      ],
      colors: {
        'editor.background': '#0d1117',
        'editor.foreground': '#c9d1d9',
        'editorLineNumber.foreground': '#484f58',
        'editorLineNumber.activeForeground': '#c9d1d9',
        'editor.selectionBackground': '#264f78',
        'editor.inactiveSelectionBackground': '#1f385c',
        'editorCursor.foreground': '#58a6ff',
        'editor.lineHighlightBackground': '#161b22',
        'editorGutter.background': '#0d1117'
      }
    });

    monaco.editor.setTheme('cloudCompilerDark');
  };

  // Sync markers when compiler diagnostics update
  useEffect(() => {
    if (monacoRef.current && editorRef.current) {
      const model = editorRef.current.getModel();
      if (model) {
        monacoRef.current.editor.setModelMarkers(model, 'compiler', editorMarkers);
      }
    }
  }, [editorMarkers]);

  const handleChange = (value) => {
    updateActiveFileContent(value || '');
    if (autosaveEnabled && activeFile) {
      // Debounced or direct auto-save state
    }
  };

  if (!activeFile) {
    return (
      <div className="flex-1 flex items-center justify-center bg-[#0d1117] text-[#8b949e] text-sm">
        Select or create a file to begin coding
      </div>
    );
  }

  return (
    <div className="flex-1 w-full h-full overflow-hidden bg-[#0d1117]">
      <Editor
        height="100%"
        defaultLanguage="c"
        language="c"
        value={activeFile.content || ''}
        onChange={handleChange}
        onMount={handleEditorDidMount}
        theme="vs-dark"
        options={{
          fontSize: 13,
          fontFamily: "'Fira Code', 'Cascadia Code', Consolas, monospace",
          fontLigatures: true,
          minimap: { enabled: true, maxColumn: 40 },
          scrollBeyondLastLine: false,
          automaticLayout: true,
          tabSize: 4,
          insertSpaces: true,
          wordWrap: 'on',
          lineNumbers: 'on',
          renderLineHighlight: 'all',
          cursorBlinking: 'smooth',
          smoothScrolling: true,
          padding: { top: 8, bottom: 8 },
        }}
      />
    </div>
  );
};
