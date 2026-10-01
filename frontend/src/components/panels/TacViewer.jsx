import React, { useState } from 'react';
import { Copy, Check, Cpu, Code2 } from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';

export const TacViewer = () => {
  const { compilerResult } = useCompiler();
  const [copied, setCopied] = useState(false);

  const tacList = compilerResult?.threeAddressCode || [];
  const rawTac = compilerResult?.tacRaw || '';

  const handleCopy = () => {
    navigator.clipboard.writeText(rawTac);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  if (tacList.length === 0) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-[#8b949e] text-xs p-4">
        <Cpu className="w-8 h-8 text-[#30363d] mb-2" />
        <p>No Three-Address Code generated.</p>
        <p className="text-[11px] text-[#484f58] mt-1">Compile your code to generate intermediate TAC instructions with temporary registers.</p>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col bg-[#0d1117] text-xs">
      {/* Header */}
      <div className="p-2 border-b border-[#21262d] bg-[#161b22]/50 flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <Code2 className="w-3.5 h-3.5 text-indigo-400" />
          <span className="font-semibold text-xs text-[#c9d1d9]">Three-Address Code (TAC) Stream</span>
          <span className="text-[11px] text-[#8b949e]">({tacList.length} instructions)</span>
        </div>

        <button
          onClick={handleCopy}
          className="px-2 py-1 bg-[#21262d] hover:bg-[#30363d] text-[#c9d1d9] rounded border border-[#30363d] flex items-center gap-1.5 transition-colors text-[11px]"
        >
          {copied ? <Check className="w-3 h-3 text-emerald-400" /> : <Copy className="w-3 h-3" />}
          <span>{copied ? 'Copied TAC' : 'Copy All'}</span>
        </button>
      </div>

      {/* Code Listing */}
      <div className="flex-1 overflow-auto p-3 font-mono text-xs leading-relaxed space-y-1">
        {tacList.map((tac, index) => {
          const isLabel = tac.label || tac.op === 'LABEL';
          const isJump = tac.op === 'GOTO' || tac.op?.startsWith('IF');

          return (
            <div
              key={index}
              className={`flex items-center px-2 py-0.5 rounded transition-colors ${
                isLabel
                  ? 'bg-amber-500/10 text-amber-300 font-bold'
                  : isJump
                  ? 'bg-indigo-500/10 text-indigo-300'
                  : 'hover:bg-[#161b22]'
              }`}
            >
              <span className="w-8 text-[#484f58] select-none text-[11px]">{index}:</span>
              <span className={isLabel ? 'pl-2 text-amber-400 font-semibold' : 'pl-6 text-[#e6edf3]'}>
                {tac.formatted || tac.toTacString || tac.op}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
};
