import React from 'react';
import { Layers, Cpu } from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';

export const QuadruplesViewer = () => {
  const { compilerResult } = useCompiler();
  const quads = compilerResult?.quadruples || [];

  if (quads.length === 0) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-[#8b949e] text-xs p-4">
        <Cpu className="w-8 h-8 text-[#30363d] mb-2" />
        <p>No Quadruples generated.</p>
        <p className="text-[11px] text-[#484f58] mt-1">Compile code to inspect standard (Op, Arg1, Arg2, Result) Quadruple records.</p>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col bg-[#0d1117] text-xs">
      <div className="p-2 border-b border-[#21262d] bg-[#161b22]/50 flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <Layers className="w-3.5 h-3.5 text-cyan-400" />
          <span className="font-semibold text-xs text-[#c9d1d9]">Quadruple Representation (Op, Arg1, Arg2, Result)</span>
        </div>
        <span className="text-[11px] text-[#8b949e]">Total: {quads.length} Quadruples</span>
      </div>

      <div className="flex-1 overflow-auto">
        <table className="w-full text-left border-collapse">
          <thead className="sticky top-0 bg-[#161b22] text-[#8b949e] border-b border-[#21262d] font-mono text-[11px]">
            <tr>
              <th className="py-1.5 px-3 w-16 text-center">Index</th>
              <th className="py-1.5 px-3">Operator</th>
              <th className="py-1.5 px-3">Argument 1</th>
              <th className="py-1.5 px-3">Argument 2</th>
              <th className="py-1.5 px-3">Result</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[#21262d]/50 font-mono text-xs">
            {quads.map((q, idx) => (
              <tr key={idx} className="hover:bg-[#161b22]/50 transition-colors">
                <td className="py-1.5 px-3 text-center text-[#484f58]">{q.index}</td>
                <td className="py-1.5 px-3 font-semibold text-rose-400">
                  <span className="bg-[#161b22] px-1.5 py-0.5 rounded border border-[#30363d]">
                    {q.op}
                  </span>
                </td>
                <td className="py-1.5 px-3 text-[#79c0ff]">{q.arg1 || '-'}</td>
                <td className="py-1.5 px-3 text-[#79c0ff]">{q.arg2 || '-'}</td>
                <td className="py-1.5 px-3 text-emerald-400 font-semibold">{q.result || '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
