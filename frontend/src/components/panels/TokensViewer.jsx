import React, { useState, useMemo } from 'react';
import { Search, Filter, Hash, Tag, Layers } from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';

export const TokensViewer = () => {
  const { compilerResult } = useCompiler();
  const [searchTerm, setSearchTerm] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('ALL');

  const tokens = compilerResult?.tokens || [];

  const categories = useMemo(() => {
    const set = new Set();
    tokens.forEach(t => {
      if (t.category) set.add(t.category);
    });
    return Array.from(set);
  }, [tokens]);

  const filteredTokens = useMemo(() => {
    return tokens.filter(t => {
      const matchesSearch = 
        t.lexeme?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        t.type?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        t.category?.toLowerCase().includes(searchTerm.toLowerCase());
      
      const matchesCat = categoryFilter === 'ALL' || t.category === categoryFilter;

      return matchesSearch && matchesCat;
    });
  }, [tokens, searchTerm, categoryFilter]);

  const getCategoryBadgeClass = (category) => {
    switch (category) {
      case 'KEYWORD':
        return 'bg-rose-500/20 text-rose-300 border-rose-500/30';
      case 'IDENTIFIER':
        return 'bg-blue-500/20 text-blue-300 border-blue-500/30';
      case 'INTEGER':
      case 'FLOAT':
      case 'CHAR':
      case 'STRING':
      case 'BOOLEAN':
        return 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30';
      case 'OPERATOR':
        return 'bg-amber-500/20 text-amber-300 border-amber-500/30';
      case 'DELIMITER':
        return 'bg-purple-500/20 text-purple-300 border-purple-500/30';
      case 'COMMENT':
        return 'bg-gray-500/20 text-gray-300 border-gray-500/30';
      case 'INVALID':
        return 'bg-red-500/40 text-red-200 border-red-500/60 font-bold';
      default:
        return 'bg-[#21262d] text-[#8b949e] border-[#30363d]';
    }
  };

  if (tokens.length === 0) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-[#8b949e] text-xs p-4">
        <Layers className="w-8 h-8 text-[#30363d] mb-2" />
        <p>No tokens generated yet.</p>
        <p className="text-[11px] text-[#484f58] mt-1">Click "Compile" or select "Lexical Analysis" from the sidebar.</p>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col bg-[#0d1117] text-xs">
      {/* Header controls: Search & Category Filter */}
      <div className="p-2 border-b border-[#21262d] flex flex-wrap items-center justify-between gap-2 bg-[#161b22]/50">
        <div className="flex items-center space-x-2 flex-1 min-w-[200px]">
          <div className="relative flex-1">
            <Search className="w-3.5 h-3.5 absolute left-2 top-2 text-[#8b949e]" />
            <input
              type="text"
              placeholder="Search token lexeme or type..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-[#0d1117] text-[#c9d1d9] text-xs rounded pl-7 pr-2 py-1 border border-[#30363d] focus:outline-none focus:border-indigo-500"
            />
          </div>

          <select
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value)}
            className="bg-[#0d1117] text-[#c9d1d9] text-xs rounded px-2 py-1 border border-[#30363d] focus:outline-none focus:border-indigo-500"
          >
            <option value="ALL">All Categories ({tokens.length})</option>
            {categories.map(cat => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </select>
        </div>

        <div className="text-[11px] text-[#8b949e]">
          Showing {filteredTokens.length} of {tokens.length} tokens
        </div>
      </div>

      {/* Table */}
      <div className="flex-1 overflow-auto">
        <table className="w-full text-left border-collapse">
          <thead className="sticky top-0 bg-[#161b22] text-[#8b949e] border-b border-[#21262d] font-mono text-[11px]">
            <tr>
              <th className="py-1.5 px-3 w-12 text-center">#</th>
              <th className="py-1.5 px-3">Category</th>
              <th className="py-1.5 px-3">Token Type</th>
              <th className="py-1.5 px-3">Lexeme</th>
              <th className="py-1.5 px-3 w-20 text-center">Line</th>
              <th className="py-1.5 px-3 w-20 text-center">Column</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[#21262d]/50 font-mono text-xs">
            {filteredTokens.map((token, index) => (
              <tr key={index} className="hover:bg-[#161b22]/50 transition-colors">
                <td className="py-1 px-3 text-[#484f58] text-center">{index + 1}</td>
                <td className="py-1 px-3">
                  <span className={`inline-block px-1.5 py-0.5 rounded text-[10px] uppercase tracking-wider border ${getCategoryBadgeClass(token.category)}`}>
                    {token.category}
                  </span>
                </td>
                <td className="py-1 px-3 text-[#79c0ff]">{token.type}</td>
                <td className="py-1 px-3 text-[#c9d1d9] font-semibold">
                  <span className="bg-[#161b22] px-1.5 py-0.5 rounded border border-[#30363d] text-amber-300">
                    {token.lexeme === '\n' ? '\\n' : token.lexeme}
                  </span>
                  {token.errorMessage && (
                    <span className="ml-2 text-red-400 text-[11px] italic font-sans">
                      ({token.errorMessage})
                    </span>
                  )}
                </td>
                <td className="py-1 px-3 text-center text-[#8b949e]">{token.line}</td>
                <td className="py-1 px-3 text-center text-[#8b949e]">{token.column}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
