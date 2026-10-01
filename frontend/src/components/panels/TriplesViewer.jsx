import React, { useState } from 'react';
import { Layers, Cpu, ArrowRight } from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';

export const TriplesViewer = () => {
  const { compilerResult } = useCompiler();
  const [viewMode, setViewMode] = useState('direct'); // 'direct' or 'indirect'

  const triples = compilerResult?.triples || [];
  const indirectTriples = compilerResult?.indirectTriples || [];

  if (triples.length === 0) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-[#8b949e] text-xs p-4">
        <Cpu className="w-8 h-8 text-[#30363d] mb-2" />
        <p>No Triples generated.</p>
        <p className="text-[11px] text-[#484f58] mt-1">Compile code to view memory-efficient Triples and Indirect Triples representations.</p>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col bg-[#0d1117] text-xs">
      {/* Header and Toggle */}
      <div className="p-2 border-b border-[#21262d] bg-[#161b22]/50 flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <Layers className="w-3.5 h-3.5 text-amber-400" />
          <span className="font-semibold text-xs text-[#c9d1d9]">
            {viewMode === 'direct' ? 'Triples Representation (Index, Op, Arg1, Arg2)' : 'Indirect Triples (Pointer Array & Statements)'}
          </span>
        </div>

        <div className="flex items-center space-x-1 bg-[#0d1117] p-0.5 rounded border border-[#30363d]">
          <button
            onClick={() => setViewMode('direct')}
            className={`px-2 py-0.5 rounded text-[11px] transition-colors ${
              viewMode === 'direct' ? 'bg-[#21262d] text-white font-medium' : 'text-[#8b949e] hover:text-[#c9d1d9]'
            }`}
          >
            Triples ({triples.length})
          </button>
          <button
            onClick={() => setViewMode('indirect')}
            className={`px-2 py-0.5 rounded text-[11px] transition-colors ${
              viewMode === 'indirect' ? 'bg-[#21262d] text-white font-medium' : 'text-[#8b949e] hover:text-[#c9d1d9]'
            }`}
          >
            Indirect Triples ({indirectTriples.length})
          </button>
        </div>
      </div>

      {/* Direct Triples Table */}
      {viewMode === 'direct' ? (
        <div className="flex-1 overflow-auto">
          <table className="w-full text-left border-collapse">
            <thead className="sticky top-0 bg-[#161b22] text-[#8b949e] border-b border-[#21262d] font-mono text-[11px]">
              <tr>
                <th className="py-1.5 px-3 w-20 text-center">Triple Index</th>
                <th className="py-1.5 px-3">Operator</th>
                <th className="py-1.5 px-3">Argument 1</th>
                <th className="py-1.5 px-3">Argument 2</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#21262d]/50 font-mono text-xs">
              {triples.map((t, idx) => (
                <tr key={idx} className="hover:bg-[#161b22]/50 transition-colors">
                  <td className="py-1.5 px-3 text-center text-amber-400 font-bold">({t.index})</td>
                  <td className="py-1.5 px-3 font-semibold text-rose-400">
                    <span className="bg-[#161b22] px-1.5 py-0.5 rounded border border-[#30363d]">
                      {t.op}
                    </span>
                  </td>
                  <td className="py-1.5 px-3 text-[#79c0ff]">{t.arg1 || '-'}</td>
                  <td className="py-1.5 px-3 text-[#79c0ff]">{t.arg2 || '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        /* Indirect Triples View */
        <div className="flex-1 overflow-auto">
          <table className="w-full text-left border-collapse">
            <thead className="sticky top-0 bg-[#161b22] text-[#8b949e] border-b border-[#21262d] font-mono text-[11px]">
              <tr>
                <th className="py-1.5 px-3 w-24 text-center">Pointer Loc</th>
                <th className="py-1.5 px-3 w-24 text-center">Triple Ref</th>
                <th className="py-1.5 px-3">Statement Representation</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#21262d]/50 font-mono text-xs">
              {indirectTriples.map((it, idx) => (
                <tr key={idx} className="hover:bg-[#161b22]/50 transition-colors">
                  <td className="py-1.5 px-3 text-center text-purple-400 font-bold">ptr[{it.pointer}]</td>
                  <td className="py-1.5 px-3 text-center text-amber-400 font-semibold">({it.tripleIndex})</td>
                  <td className="py-1.5 px-3 text-[#e6edf3]">
                    <span className="bg-[#161b22] px-2 py-0.5 rounded border border-[#30363d] text-emerald-300">
                      {it.statement}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};
