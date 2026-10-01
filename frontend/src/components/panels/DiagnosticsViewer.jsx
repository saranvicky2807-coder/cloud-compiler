import React from 'react';
import { AlertCircle, AlertTriangle, CheckCircle2, ChevronRight } from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';

export const DiagnosticsViewer = () => {
  const { compilerResult } = useCompiler();

  const syntaxErrors = compilerResult?.syntaxErrors || [];
  const semanticErrors = compilerResult?.semanticErrors || [];
  const totalErrors = syntaxErrors.length + semanticErrors.length;

  if (totalErrors === 0 && compilerResult?.success) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-emerald-400 text-xs p-4">
        <CheckCircle2 className="w-8 h-8 text-emerald-500 mb-2" />
        <p className="font-semibold text-sm">No Errors or Warnings Detected</p>
        <p className="text-[11px] text-[#8b949e] mt-1">Source code successfully passed Lexical, Syntax, and Semantic verification.</p>
      </div>
    );
  }

  if (totalErrors === 0 && !compilerResult) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-[#8b949e] text-xs p-4">
        <AlertCircle className="w-8 h-8 text-[#30363d] mb-2" />
        <p>No diagnostics available.</p>
        <p className="text-[11px] text-[#484f58] mt-1">Run compilation to see syntax and semantic error reports.</p>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col bg-[#0d1117] text-xs divide-y divide-[#21262d]">
      <div className="p-2 bg-[#161b22]/50 flex items-center justify-between">
        <span className="font-semibold text-xs text-rose-400 flex items-center gap-1.5">
          <AlertCircle className="w-3.5 h-3.5" />
          Diagnostics & Error List ({totalErrors})
        </span>
      </div>

      <div className="flex-1 overflow-auto p-3 space-y-2">
        {/* Syntax Errors */}
        {syntaxErrors.length > 0 && (
          <div>
            <div className="text-[11px] font-semibold text-amber-400 uppercase tracking-wider mb-1 flex items-center gap-1">
              <AlertTriangle className="w-3 h-3" />
              Syntax Errors ({syntaxErrors.length})
            </div>
            <div className="space-y-1.5 pl-2">
              {syntaxErrors.map((err, idx) => (
                <div
                  key={idx}
                  className="p-2 rounded bg-rose-500/10 border border-rose-500/30 text-rose-200"
                >
                  <div className="flex items-center justify-between font-mono text-[11px] text-rose-300 mb-0.5">
                    <span>Line {err.line}, Column {err.column}</span>
                    <span className="bg-rose-950 px-1 rounded text-[10px]">SYNTAX_ERROR</span>
                  </div>
                  <div className="text-xs font-semibold">{err.message}</div>
                  <div className="text-[11px] text-[#8b949e] mt-1 font-mono">
                    Found: <span className="text-white bg-[#161b22] px-1 rounded">{err.found}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Semantic Errors */}
        {semanticErrors.length > 0 && (
          <div>
            <div className="text-[11px] font-semibold text-purple-400 uppercase tracking-wider mb-1 flex items-center gap-1">
              <AlertCircle className="w-3 h-3" />
              Semantic Errors ({semanticErrors.length})
            </div>
            <div className="space-y-1.5 pl-2">
              {semanticErrors.map((err, idx) => (
                <div
                  key={idx}
                  className="p-2 rounded bg-purple-500/10 border border-purple-500/30 text-purple-200"
                >
                  <div className="flex items-center justify-between font-mono text-[11px] text-purple-300 mb-0.5">
                    <span>Line {err.line}, Column {err.column}</span>
                    <span className="bg-purple-950 px-1 rounded text-[10px]">{err.errorType}</span>
                  </div>
                  <div className="text-xs font-semibold">{err.message}</div>
                  {err.expectedType && (
                    <div className="text-[11px] text-[#8b949e] mt-1 font-mono">
                      Expected type: <span className="text-emerald-400">{err.expectedType}</span> | Received: <span className="text-rose-400">{err.foundType}</span>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
