import React from 'react';
import { Terminal, Check, X, Clock, AlertTriangle, Cpu } from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';

export const TerminalLogger = () => {
  const { compilerResult, isCompiling } = useCompiler();

  const stages = compilerResult?.stages || [];

  return (
    <div className="h-full flex flex-col bg-[#0d1117] text-xs font-mono">
      <div className="p-2 border-b border-[#21262d] bg-[#161b22]/50 flex items-center justify-between font-sans">
        <div className="flex items-center space-x-2">
          <Terminal className="w-3.5 h-3.5 text-indigo-400" />
          <span className="font-semibold text-xs text-[#c9d1d9]">Compilation Pipeline Log</span>
        </div>
        {compilerResult && (
          <span className="text-[11px] text-[#8b949e]">
            Total Time: {compilerResult.totalExecutionTimeMs}ms
          </span>
        )}
      </div>

      <div className="flex-1 overflow-auto p-3 space-y-3 font-mono">
        {isCompiling ? (
          <div className="space-y-2 text-[#8b949e]">
            <div className="flex items-center space-x-2 text-indigo-400">
              <Cpu className="w-4 h-4 animate-spin" />
              <span>Running compiler stages...</span>
            </div>
            <div className="pl-6 space-y-1 text-[11px]">
              <div>&gt; Initializing Lexer token stream...</div>
              <div>&gt; Constructing AST grammar nodes...</div>
              <div>&gt; Checking semantic scopes and type rules...</div>
              <div>&gt; Generating Three-Address Code and Quadruples...</div>
            </div>
          </div>
        ) : stages.length > 0 ? (
          <div className="space-y-2.5">
            <div className="text-[11px] text-[#8b949e] border-b border-[#21262d] pb-1">
              {compilerResult?.summary}
            </div>

            {stages.map((stage, idx) => {
              const isSuccess = stage.status === 'SUCCESS';
              const isWarning = stage.status === 'WARNING';
              const isFailed = stage.status === 'FAILED';

              return (
                <div key={idx} className="space-y-1">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-2">
                      {isSuccess ? (
                        <Check className="w-3.5 h-3.5 text-emerald-400 shrink-0" />
                      ) : isWarning ? (
                        <AlertTriangle className="w-3.5 h-3.5 text-amber-400 shrink-0" />
                      ) : (
                        <X className="w-3.5 h-3.5 text-rose-400 shrink-0" />
                      )}
                      <span className={`font-semibold ${isSuccess ? 'text-emerald-300' : isWarning ? 'text-amber-300' : 'text-rose-300'}`}>
                        {idx + 1}. {stage.stageName}
                      </span>
                    </div>

                    <span className="text-[10px] text-[#8b949e]">
                      {stage.durationMs}ms
                    </span>
                  </div>

                  <div className="pl-6 text-[11px] text-[#8b949e]">
                    {stage.message}
                  </div>
                </div>
              );
            })}

            <div className="pt-2 border-t border-[#21262d] text-[11px] text-indigo-300">
              [Pipeline Process Completed with exit code {compilerResult?.success ? '0' : '1'}]
            </div>
          </div>
        ) : (
          <div className="text-[#8b949e] text-[11px] py-4 text-center">
            Press "Compile" (or Ctrl+Enter) in the code editor to start compiler execution log.
          </div>
        )}
      </div>
    </div>
  );
};
