import React, { useState } from 'react';
import { 
  GitBranch, 
  ChevronRight, 
  ChevronDown, 
  Search, 
  Maximize2, 
  Minimize2, 
  Info, 
  Box 
} from 'lucide-react';
import { useCompiler } from '../../context/CompilerContext';

const AstTreeNode = ({ node, level = 0, selectedNode, onSelectNode, isExpandedAll }) => {
  const [collapsed, setCollapsed] = useState(false);

  if (!node) return null;

  const hasChildren = node.children && node.children.length > 0;
  const isSelected = selectedNode?.id === node.id;

  const getNodeColor = (nodeType) => {
    switch (nodeType) {
      case 'Program':
        return 'text-indigo-400 bg-indigo-500/10 border-indigo-500/30';
      case 'FunctionDeclaration':
        return 'text-purple-400 bg-purple-500/10 border-purple-500/30';
      case 'VariableDeclaration':
        return 'text-blue-400 bg-blue-500/10 border-blue-500/30';
      case 'Assignment':
        return 'text-cyan-400 bg-cyan-500/10 border-cyan-500/30';
      case 'IfStatement':
      case 'WhileStatement':
      case 'ForStatement':
        return 'text-amber-400 bg-amber-500/10 border-amber-500/30';
      case 'BinaryExpression':
      case 'UnaryExpression':
        return 'text-emerald-400 bg-emerald-500/10 border-emerald-500/30';
      case 'Literal':
        return 'text-rose-400 bg-rose-500/10 border-rose-500/30';
      case 'Identifier':
        return 'text-sky-300 bg-sky-500/10 border-sky-500/30';
      default:
        return 'text-[#c9d1d9] bg-[#21262d] border-[#30363d]';
    }
  };

  return (
    <div className="text-xs">
      <div
        onClick={() => onSelectNode(node)}
        className={`flex items-center space-x-1.5 py-1 px-2 rounded cursor-pointer transition-colors group font-mono ${
          isSelected
            ? 'bg-indigo-600/30 border border-indigo-500/50 text-white'
            : 'hover:bg-[#161b22]'
        }`}
        style={{ paddingLeft: `${level * 16 + 8}px` }}
      >
        {hasChildren ? (
          <button
            onClick={(e) => {
              e.stopPropagation();
              setCollapsed(!collapsed);
            }}
            className="p-0.5 hover:bg-[#30363d] rounded text-[#8b949e]"
          >
            {collapsed ? (
              <ChevronRight className="w-3.5 h-3.5" />
            ) : (
              <ChevronDown className="w-3.5 h-3.5" />
            )}
          </button>
        ) : (
          <span className="w-4 h-4 inline-block" />
        )}

        <span className={`px-1.5 py-0.5 rounded text-[10px] uppercase font-bold border ${getNodeColor(node.nodeType)}`}>
          {node.nodeType}
        </span>

        <span className="text-[#c9d1d9] truncate font-medium">
          {node.label || node.nodeType}
        </span>

        {node.line > 0 && (
          <span className="text-[10px] text-[#484f58] ml-auto">
            L{node.line}:C{node.column}
          </span>
        )}
      </div>

      {hasChildren && !collapsed && (
        <div className="border-l border-[#21262d] ml-3">
          {node.children.map((child, idx) => (
            <AstTreeNode
              key={child.id || idx}
              node={child}
              level={level + 1}
              selectedNode={selectedNode}
              onSelectNode={onSelectNode}
              isExpandedAll={isExpandedAll}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export const AstViewer = () => {
  const { compilerResult } = useCompiler();
  const [selectedNode, setSelectedNode] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  const ast = compilerResult?.ast;

  if (!ast) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-[#8b949e] text-xs p-4">
        <GitBranch className="w-8 h-8 text-[#30363d] mb-2" />
        <p>No Abstract Syntax Tree generated.</p>
        <p className="text-[11px] text-[#484f58] mt-1">Compile code to inspect the generated syntax grammar tree.</p>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col md:flex-row bg-[#0d1117] text-xs overflow-hidden">
      {/* Left Tree Panel */}
      <div className="flex-1 flex flex-col border-r border-[#21262d] overflow-hidden">
        <div className="p-2 border-b border-[#21262d] bg-[#161b22]/50 flex items-center justify-between">
          <span className="font-semibold text-xs text-[#c9d1d9] flex items-center gap-1.5">
            <GitBranch className="w-3.5 h-3.5 text-emerald-400" />
            Abstract Syntax Tree Structure
          </span>
          <span className="text-[11px] text-[#8b949e]">
            Root: {ast.nodeType}
          </span>
        </div>

        <div className="flex-1 overflow-auto p-2 space-y-0.5">
          <AstTreeNode
            node={ast}
            level={0}
            selectedNode={selectedNode}
            onSelectNode={setSelectedNode}
          />
        </div>
      </div>

      {/* Right Node Property Inspector */}
      <div className="w-full md:w-72 bg-[#161b22]/40 p-3 overflow-y-auto border-t md:border-t-0 border-[#21262d]">
        <div className="flex items-center space-x-1.5 text-xs font-semibold text-[#c9d1d9] mb-3 pb-2 border-b border-[#21262d]">
          <Info className="w-3.5 h-3.5 text-indigo-400" />
          <span>Node Inspector</span>
        </div>

        {selectedNode ? (
          <div className="space-y-3 font-mono text-xs">
            <div>
              <span className="text-[10px] text-[#8b949e] uppercase block font-sans">Node Type</span>
              <span className="text-indigo-300 font-bold">{selectedNode.nodeType}</span>
            </div>

            <div>
              <span className="text-[10px] text-[#8b949e] uppercase block font-sans">Label / Expression</span>
              <span className="text-[#c9d1d9] bg-[#0d1117] p-1.5 rounded block border border-[#30363d]">
                {selectedNode.label || selectedNode.nodeType}
              </span>
            </div>

            <div className="grid grid-cols-2 gap-2">
              <div>
                <span className="text-[10px] text-[#8b949e] uppercase block font-sans">Line</span>
                <span className="text-[#c9d1d9]">{selectedNode.line}</span>
              </div>
              <div>
                <span className="text-[10px] text-[#8b949e] uppercase block font-sans">Column</span>
                <span className="text-[#c9d1d9]">{selectedNode.column}</span>
              </div>
            </div>

            {selectedNode.attributes && Object.keys(selectedNode.attributes).length > 0 && (
              <div>
                <span className="text-[10px] text-[#8b949e] uppercase block font-sans mb-1">Attributes</span>
                <div className="bg-[#0d1117] rounded border border-[#30363d] divide-y divide-[#21262d] text-[11px]">
                  {Object.entries(selectedNode.attributes).map(([k, v]) => (
                    <div key={k} className="p-1.5 flex justify-between">
                      <span className="text-[#8b949e]">{k}:</span>
                      <span className="text-amber-300">{String(v)}</span>
                    </div>
                  ))}
                </div>
              </div>
            )}

            <div>
              <span className="text-[10px] text-[#8b949e] uppercase block font-sans">Children Count</span>
              <span className="text-[#c9d1d9]">{selectedNode.children?.length || 0} sub-nodes</span>
            </div>
          </div>
        ) : (
          <div className="text-[#8b949e] text-[11px] text-center py-6">
            Click on any AST node in the tree to inspect its grammar attributes and metadata.
          </div>
        )}
      </div>
    </div>
  );
};
