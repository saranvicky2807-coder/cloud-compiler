import React from 'react';
import { Terminal, Check, AlertCircle, Cpu, Clock, GitBranch } from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';
import { useProject } from '../../context/ProjectContext';

export const Footer = () => {
  const { compilerResult, isCompiling, activeOutputTab, setActiveOutputTab } = useCompiler();
  const { activeFile } = useProject();

  const totalErrors = compilerResult?.totalErrors || 0;
  const isSuccess = compilerResult?.success;

  return (
    <footer className="h-6 bg-[#161b22] border-t border-[#21262d] px-3 flex items-center justify-between text-[11px] text-[#8b949e] select-none z-20">
      {/* Left: Terminal Tab toggle & Status */}
      <div className="flex items-center space-x-3">
        <button
          onClick={() => setActiveOutputTab('terminal')}
          className={`flex items-center space-x-1 hover:text-white transition-colors ${
            activeOutputTab === 'terminal' ? 'text-indigo-400 font-semibold' : ''
          }`}
        >
          <Terminal className="w-3 h-3" />
          <span>Compilation Log</span>
        </button>

        <div className="h-3 w-[1px] bg-[#30363d]" />

        {isCompiling ? (
          <span className="text-amber-400 flex items-center gap-1 animate-pulse">
            <Cpu className="w-3 h-3 animate-spin" /> Compiling AST & TAC...
          </span>
        ) : compilerResult ? (
          isSuccess ? (
            <span className="text-emerald-400 flex items-center gap-1">
              <Check className="w-3 h-3" /> Ready • 0 Errors
            </span>
          ) : (
            <button
              onClick={() => setActiveOutputTab('errors')}
              className="text-rose-400 flex items-center gap-1 hover:underline"
            >
              <AlertCircle className="w-3 h-3" /> {totalErrors} Error(s)
            </button>
          )
        ) : (
          <span>Ready</span>
        )}
      </div>

      {/* Right: Metadata */}
      <div className="flex items-center space-x-4">
        {compilerResult && (
          <span className="flex items-center gap-1">
            <Clock className="w-3 h-3" /> {compilerResult.totalExecutionTimeMs}ms
          </span>
        )}
        <span>Language: C-Like</span>
        <span>UTF-8</span>
        <span>Spaces: 4</span>
      </div>
    </footer>
  );
};
