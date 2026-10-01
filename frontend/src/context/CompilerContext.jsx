import React, { createContext, useContext, useState } from 'react';
import { compilerService } from '../services/compilerService';
import { useProject } from './ProjectContext';

const CompilerContext = createContext(null);

export const CompilerProvider = ({ children }) => {
  const { activeFile, activeProject, saveFile } = useProject();
  const [isCompiling, setIsCompiling] = useState(false);
  const [compilerResult, setCompilerResult] = useState(null);
  const [activeOutputTab, setActiveOutputTab] = useState('tokens'); // tokens, ast, symbol, tac, quad, triple, errors, terminal
  const [editorMarkers, setEditorMarkers] = useState([]);

  const runCompilation = async (targetStage = 'ALL') => {
    if (!activeFile || !activeFile.content) return;

    setIsCompiling(true);
    // Optionally auto-save active file if dirty
    if (activeFile.isDirty) {
      saveFile(activeFile.id);
    }

    try {
      const payload = {
        sourceCode: activeFile.content,
        fileName: activeFile.name,
        projectId: activeProject?.id || null,
        fileId: activeFile.id && !String(activeFile.id).startsWith('local-') && !String(activeFile.id).startsWith('sample-') ? activeFile.id : null,
        targetStage,
      };

      let result;
      if (targetStage === 'LEXICAL') {
        result = await compilerService.tokenize(payload);
      } else if (targetStage === 'SYNTAX') {
        result = await compilerService.parse(payload);
      } else if (targetStage === 'SEMANTIC') {
        result = await compilerService.analyze(payload);
      } else if (targetStage === 'IR') {
        result = await compilerService.generateIr(payload);
      } else {
        result = await compilerService.compile(payload);
      }

      setCompilerResult(result);

      // Map syntax & semantic errors to Monaco Editor Markers
      const markers = [];
      if (result.syntaxErrors) {
        result.syntaxErrors.forEach(err => {
          markers.push({
            severity: 8, // MarkerSeverity.Error
            startLineNumber: err.line || 1,
            startColumn: err.column || 1,
            endLineNumber: err.line || 1,
            endColumn: (err.column || 1) + (err.found ? err.found.length : 1),
            message: `Syntax Error: ${err.message}`,
          });
        });
      }

      if (result.semanticErrors) {
        result.semanticErrors.forEach(err => {
          markers.push({
            severity: 8, // MarkerSeverity.Error
            startLineNumber: err.line || 1,
            startColumn: err.column || 1,
            endLineNumber: err.line || 1,
            endColumn: (err.column || 1) + (err.identifier ? err.identifier.length : 1),
            message: `Semantic Error: ${err.message}`,
          });
        });
      }

      setEditorMarkers(markers);

      // Auto-switch to errors tab if there are errors and user was not already on a specific tab
      if (markers.length > 0 && activeOutputTab !== 'terminal') {
        setActiveOutputTab('errors');
      }
    } catch (err) {
      console.error('Compilation failed:', err);
      setCompilerResult({
        success: false,
        totalErrors: 1,
        summary: 'Failed to communicate with compiler backend service.',
        stages: [
          {
            stageName: 'Connection Error',
            status: 'FAILED',
            durationMs: 0,
            message: err.message || 'Could not connect to compiler API'
          }
        ]
      });
      setActiveOutputTab('terminal');
    } finally {
      setIsCompiling(false);
    }
  };

  return (
    <CompilerContext.Provider
      value={{
        isCompiling,
        compilerResult,
        activeOutputTab,
        editorMarkers,
        setActiveOutputTab,
        runCompilation,
        setCompilerResult,
      }}
    >
      {children}
    </CompilerContext.Provider>
  );
};

export const useCompiler = () => useContext(CompilerContext);
