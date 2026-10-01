import React from 'react';
import { 
  Binary, 
  GitBranch, 
  Table, 
  Cpu, 
  Layers, 
  Terminal, 
  AlertCircle, 
  ChevronUp, 
  ChevronDown 
} from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';
import { TokensViewer } from './TokensViewer';
import { AstViewer } from './AstViewer';
import { SymbolTableViewer } from './SymbolTableViewer';
import { TacViewer } from './TacViewer';
import { QuadruplesViewer } from './QuadruplesViewer';
import { TriplesViewer } from './TriplesViewer';
import { DiagnosticsViewer } from './DiagnosticsViewer';
import { TerminalLogger } from './TerminalLogger';

export const OutputPanel = () => {
  const { activeOutputTab, setActiveOutputTab, compilerResult } = useCompiler();

  const totalErrors = compilerResult?.totalErrors || 0;
  const tokenCount = compilerResult?.tokens?.length || 0;
  const symbolCount = compilerResult?.symbolTable?.length || 0;
  const tacCount = compilerResult?.threeAddressCode?.length || 0;

  const tabs = [
    { id: 'tokens', label: 'Tokens', icon: Binary, count: tokenCount },
    { id: 'ast', label: 'AST Visualizer', icon: GitBranch },
    { id: 'symbol', label: 'Symbol Table', icon: Table, count: symbolCount },
    { id: 'tac', label: 'Three-Address Code', icon: Cpu, count: tacCount },
    { id: 'quad', label: 'Quadruples', icon: Layers },
    { id: 'triple', label: 'Triples', icon: Layers },
    { id: 'errors', label: 'Diagnostics', icon: AlertCircle, count: totalErrors, isError: totalErrors > 0 },
    { id: 'terminal', label: 'Pipeline Log', icon: Terminal }
  ];

  return (
    <div className="h-full flex flex-col bg-[#0d1117] border-l border-[#21262d] overflow-hidden select-none">
      {/* Tab Navigation */}
      <div className="h-9 bg-[#161b22] border-b border-[#21262d] flex items-center px-1 overflow-x-auto space-x-0.5">
        {tabs.map((tab) => {
          const Icon = tab.icon;
          const isActive = activeOutputTab === tab.id;

          return (
            <button
              key={tab.id}
              onClick={() => setActiveOutputTab(tab.id)}
              className={`h-full px-2.5 flex items-center space-x-1.5 text-xs font-medium border-b-2 transition-colors shrink-0 ${
                isActive
                  ? 'border-indigo-500 text-white bg-[#0d1117]/80'
                  : 'border-transparent text-[#8b949e] hover:text-[#c9d1d9] hover:bg-[#21262d]/50'
              }`}
            >
              <Icon className={`w-3.5 h-3.5 ${isActive ? 'text-indigo-400' : 'text-[#8b949e]'}`} />
              <span>{tab.label}</span>
              {tab.count !== undefined && tab.count > 0 && (
                <span className={`text-[10px] px-1 py-0.2 rounded-full ${
                  tab.isError ? 'bg-rose-500/20 text-rose-300' : 'bg-[#21262d] text-[#8b949e]'
                }`}>
                  {tab.count}
                </span>
              )}
            </button>
          );
        })}
      </div>

      {/* Panel Content Body */}
      <div className="flex-1 overflow-hidden">
        {activeOutputTab === 'tokens' && <TokensViewer />}
        {activeOutputTab === 'ast' && <AstViewer />}
        {activeOutputTab === 'symbol' && <SymbolTableViewer />}
        {activeOutputTab === 'tac' && <TacViewer />}
        {activeOutputTab === 'quad' && <QuadruplesViewer />}
        {activeOutputTab === 'triple' && <TriplesViewer />}
        {activeOutputTab === 'errors' && <DiagnosticsViewer />}
        {activeOutputTab === 'terminal' && <TerminalLogger />}
      </div>
    </div>
  );
};
