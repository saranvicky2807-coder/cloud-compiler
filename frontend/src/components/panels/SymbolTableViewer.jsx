import React, { useState, useMemo } from 'react';
import { Table, Search, Filter, ShieldCheck, Database, Layers } from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';

export const SymbolTableViewer = () => {
  const { compilerResult } = useCompiler();
  const [searchTerm, setSearchTerm] = useState('');
  const [scopeFilter, setScopeFilter] = useState('ALL');

  const symbols = compilerResult?.symbolTable || [];

  const scopes = useMemo(() => {
    const set = new Set();
    symbols.forEach(s => {
      if (s.scopeName) set.add(s.scopeName);
    });
    return Array.from(set);
  }, [symbols]);

  const filteredSymbols = useMemo(() => {
    return symbols.filter(s => {
      const matchesSearch =
        s.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        s.type?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        s.scopeName?.toLowerCase().includes(searchTerm.toLowerCase());

      const matchesScope = scopeFilter === 'ALL' || s.scopeName === scopeFilter;

      return matchesSearch && matchesScope;
    });
  }, [symbols, searchTerm, scopeFilter]);

  const getTypeBadgeClass = (type) => {
    switch (type?.toLowerCase()) {
      case 'int':
        return 'bg-blue-500/20 text-blue-300 border-blue-500/30';
      case 'float':
        return 'bg-cyan-500/20 text-cyan-300 border-cyan-500/30';
      case 'char':
        return 'bg-amber-500/20 text-amber-300 border-amber-500/30';
      case 'bool':
        return 'bg-purple-500/20 text-purple-300 border-purple-500/30';
      case 'void':
        return 'bg-gray-500/20 text-gray-300 border-gray-500/30';
      default:
        return 'bg-indigo-500/20 text-indigo-300 border-indigo-500/30';
    }
  };

  if (symbols.length === 0) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-[#8b949e] text-xs p-4">
        <Table className="w-8 h-8 text-[#30363d] mb-2" />
        <p>No Symbol Table generated.</p>
        <p className="text-[11px] text-[#484f58] mt-1">Compile code to inspect resolved identifier symbols and scopes.</p>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col bg-[#0d1117] text-xs">
      {/* Search & Scope Filter Controls */}
      <div className="p-2 border-b border-[#21262d] flex flex-wrap items-center justify-between gap-2 bg-[#161b22]/50">
        <div className="flex items-center space-x-2 flex-1 min-w-[200px]">
          <div className="relative flex-1">
            <Search className="w-3.5 h-3.5 absolute left-2 top-2 text-[#8b949e]" />
            <input
              type="text"
              placeholder="Search symbol name, type, scope..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-[#0d1117] text-[#c9d1d9] text-xs rounded pl-7 pr-2 py-1 border border-[#30363d] focus:outline-none focus:border-indigo-500"
            />
          </div>

          <select
            value={scopeFilter}
            onChange={(e) => setScopeFilter(e.target.value)}
            className="bg-[#0d1117] text-[#c9d1d9] text-xs rounded px-2 py-1 border border-[#30363d] focus:outline-none focus:border-indigo-500"
          >
            <option value="ALL">All Scopes ({symbols.length})</option>
            {scopes.map(sc => (
              <option key={sc} value={sc}>{sc}</option>
            ))}
          </select>
        </div>

        <div className="text-[11px] text-[#8b949e]">
          Showing {filteredSymbols.length} of {symbols.length} symbols
        </div>
      </div>

      {/* Table */}
      <div className="flex-1 overflow-auto">
        <table className="w-full text-left border-collapse">
          <thead className="sticky top-0 bg-[#161b22] text-[#8b949e] border-b border-[#21262d] font-mono text-[11px]">
            <tr>
              <th className="py-1.5 px-3">Symbol Name</th>
              <th className="py-1.5 px-3">Type</th>
              <th className="py-1.5 px-3">Kind</th>
              <th className="py-1.5 px-3">Scope</th>
              <th className="py-1.5 px-3 text-center">Scope Level</th>
              <th className="py-1.5 px-3 text-center">Offset / Size</th>
              <th className="py-1.5 px-3 text-center">Ref Count</th>
              <th className="py-1.5 px-3 text-center">Line</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[#21262d]/50 font-mono text-xs">
            {filteredSymbols.map((sym, index) => (
              <tr key={index} className="hover:bg-[#161b22]/50 transition-colors">
                <td className="py-1.5 px-3 font-semibold text-white">
                  <span className="text-indigo-400">{sym.name}</span>
                </td>
                <td className="py-1.5 px-3">
                  <span className={`inline-block px-1.5 py-0.5 rounded text-[10px] uppercase font-bold border ${getTypeBadgeClass(sym.type)}`}>
                    {sym.type}
                  </span>
                </td>
                <td className="py-1.5 px-3 text-[#8b949e]">
                  {sym.isFunction ? (
                    <span className="text-purple-300">Function ({sym.parameterTypes?.join(', ') || 'void'})</span>
                  ) : (
                    <span className="text-emerald-300">Variable</span>
                  )}
                </td>
                <td className="py-1.5 px-3 text-[#c9d1d9]">
                  <span className="bg-[#161b22] px-1.5 py-0.5 rounded border border-[#30363d] text-amber-300">
                    {sym.scopeName}
                  </span>
                </td>
                <td className="py-1.5 px-3 text-center text-[#8b949e]">{sym.scopeLevel}</td>
                <td className="py-1.5 px-3 text-center text-[#8b949e]">
                  {sym.isFunction ? '-' : `Offset: ${sym.memoryOffset} (${sym.sizeBytes}B)`}
                </td>
                <td className="py-1.5 px-3 text-center text-[#79c0ff]">{sym.referenceCount}</td>
                <td className="py-1.5 px-3 text-center text-[#8b949e]">{sym.line}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
