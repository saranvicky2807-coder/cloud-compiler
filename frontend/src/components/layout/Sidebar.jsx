import React, { useState } from 'react';
import { 
  Folder, 
  FolderPlus, 
  FileCode, 
  FilePlus, 
  Trash2, 
  ChevronRight, 
  ChevronDown, 
  Play, 
  Binary, 
  GitBranch, 
  Table, 
  Cpu, 
  Code,
  Sparkles,
  RefreshCw,
  Plus
} from 'lucide-react';
import { useProject } from '../../context/ProjectContext';
import { useCompiler } from '../../context/CompilerContext';
import { useAuth } from '../../context/AuthContext';
import { SAMPLE_PROGRAMS } from '../../utils/samplePrograms';

export const Sidebar = ({ onOpenNewProject, onOpenNewFile, onOpenTemplates }) => {
  const { isAuthenticated } = useAuth();
  const { 
    projects, 
    activeProject, 
    selectProject, 
    deleteProject, 
    openFiles, 
    activeFileId, 
    setActiveFileId, 
    deleteFile, 
    createFile,
    loadSampleProgram,
    loadingProjects,
    loadProjects
  } = useProject();

  const { runCompilation, isCompiling, setActiveOutputTab } = useCompiler();

  const [collapsedSections, setCollapsedSections] = useState({
    explorer: false,
    pipeline: false,
    samples: false
  });

  const toggleSection = (key) => {
    setCollapsedSections(prev => ({ ...prev, [key]: !prev[key] }));
  };

  const handleStageRun = (stage, tab) => {
    setActiveOutputTab(tab);
    runCompilation(stage);
  };

  return (
    <aside className="w-64 bg-[#161b22] border-r border-[#21262d] flex flex-col h-full select-none text-xs">
      {/* Top Project Selector / Header */}
      <div className="p-2 border-b border-[#21262d] bg-[#0d1117]/50">
        <div className="flex items-center justify-between mb-1.5">
          <span className="text-[11px] font-semibold uppercase tracking-wider text-[#8b949e]">
            {isAuthenticated ? 'Active Project' : 'Playground Mode'}
          </span>
          <div className="flex items-center space-x-1">
            {isAuthenticated && (
              <>
                <button
                  onClick={loadProjects}
                  className="p-1 text-[#8b949e] hover:text-white hover:bg-[#21262d] rounded transition-colors"
                  title="Refresh Projects"
                >
                  <RefreshCw className={`w-3 h-3 ${loadingProjects ? 'animate-spin' : ''}`} />
                </button>
                <button
                  onClick={onOpenNewProject}
                  className="p-1 text-[#8b949e] hover:text-white hover:bg-[#21262d] rounded transition-colors"
                  title="New Project"
                >
                  <FolderPlus className="w-3 h-3" />
                </button>
              </>
            )}
          </div>
        </div>

        {isAuthenticated && projects.length > 0 ? (
          <select
            value={activeProject?.id || ''}
            onChange={(e) => {
              const proj = projects.find(p => p.id === e.target.value);
              if (proj) selectProject(proj);
            }}
            className="w-full bg-[#21262d] text-[#c9d1d9] text-xs rounded px-2 py-1 border border-[#30363d] focus:outline-none focus:border-indigo-500"
          >
            {projects.map(p => (
              <option key={p.id} value={p.id}>{p.name}</option>
            ))}
          </select>
        ) : (
          <div className="flex items-center justify-between text-xs text-[#8b949e] px-1 py-0.5">
            <span>{isAuthenticated ? 'No projects yet' : 'In-Memory Workspace'}</span>
            {isAuthenticated && (
              <button
                onClick={onOpenNewProject}
                className="text-indigo-400 hover:text-indigo-300 flex items-center gap-1 text-[11px]"
              >
                <Plus className="w-3 h-3" /> Create
              </button>
            )}
          </div>
        )}
      </div>

      {/* Main Scrollable Tree */}
      <div className="flex-1 overflow-y-auto overflow-x-hidden p-2 space-y-3">
        {/* Section 1: Files Explorer */}
        <div>
          <div
            onClick={() => toggleSection('explorer')}
            className="flex items-center justify-between text-[#8b949e] hover:text-white cursor-pointer py-1 px-1 rounded hover:bg-[#21262d]/40 transition-colors font-semibold"
          >
            <div className="flex items-center space-x-1">
              {collapsedSections.explorer ? (
                <ChevronRight className="w-3.5 h-3.5" />
              ) : (
                <ChevronDown className="w-3.5 h-3.5" />
              )}
              <span className="uppercase text-[10px] tracking-wider">Source Files</span>
            </div>
            <button
              onClick={(e) => {
                e.stopPropagation();
                onOpenNewFile();
              }}
              className="p-0.5 text-[#8b949e] hover:text-white hover:bg-[#21262d] rounded"
              title="Add File"
            >
              <FilePlus className="w-3.5 h-3.5" />
            </button>
          </div>

          {!collapsedSections.explorer && (
            <div className="mt-1 space-y-0.5 pl-2">
              {openFiles.length === 0 ? (
                <p className="text-[11px] text-[#484f58] italic py-1">No open files</p>
              ) : (
                openFiles.map(file => {
                  const isActive = file.id === activeFileId;
                  return (
                    <div
                      key={file.id}
                      onClick={() => setActiveFileId(file.id)}
                      className={`group flex items-center justify-between px-2 py-1 rounded cursor-pointer transition-colors ${
                        isActive
                          ? 'bg-[#21262d] text-white font-medium'
                          : 'text-[#8b949e] hover:bg-[#21262d]/50 hover:text-[#c9d1d9]'
                      }`}
                    >
                      <div className="flex items-center space-x-1.5 truncate">
                        <FileCode className={`w-3.5 h-3.5 shrink-0 ${isActive ? 'text-indigo-400' : 'text-[#8b949e]'}`} />
                        <span className="truncate">{file.name}</span>
                        {file.isDirty && (
                          <span className="w-1.5 h-1.5 rounded-full bg-amber-400 shrink-0" title="Unsaved changes" />
                        )}
                      </div>

                      {openFiles.length > 1 && (
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            deleteFile(file.id);
                          }}
                          className="opacity-0 group-hover:opacity-100 p-0.5 text-[#8b949e] hover:text-rose-400 rounded transition-opacity"
                          title="Delete File"
                        >
                          <Trash2 className="w-3 h-3" />
                        </button>
                      )}
                    </div>
                  );
                })
              )}
            </div>
          )}
        </div>

        {/* Section 2: Interactive Compiler Pipeline Stages */}
        <div>
          <div
            onClick={() => toggleSection('pipeline')}
            className="flex items-center justify-between text-[#8b949e] hover:text-white cursor-pointer py-1 px-1 rounded hover:bg-[#21262d]/40 transition-colors font-semibold"
          >
            <div className="flex items-center space-x-1">
              {collapsedSections.pipeline ? (
                <ChevronRight className="w-3.5 h-3.5" />
              ) : (
                <ChevronDown className="w-3.5 h-3.5" />
              )}
              <span className="uppercase text-[10px] tracking-wider">Compiler Pipeline</span>
            </div>
          </div>

          {!collapsedSections.pipeline && (
            <div className="mt-1 space-y-1 pl-2">
              <button
                onClick={() => handleStageRun('LEXICAL', 'tokens')}
                disabled={isCompiling}
                className="w-full text-left flex items-center justify-between px-2 py-1.5 rounded hover:bg-[#21262d] text-[#c9d1d9] transition-colors"
              >
                <div className="flex items-center space-x-2">
                  <Binary className="w-3.5 h-3.5 text-blue-400" />
                  <span>1. Lexical Analysis</span>
                </div>
                <span className="text-[10px] text-[#8b949e]">Tokens</span>
              </button>

              <button
                onClick={() => handleStageRun('SYNTAX', 'ast')}
                disabled={isCompiling}
                className="w-full text-left flex items-center justify-between px-2 py-1.5 rounded hover:bg-[#21262d] text-[#c9d1d9] transition-colors"
              >
                <div className="flex items-center space-x-2">
                  <GitBranch className="w-3.5 h-3.5 text-emerald-400" />
                  <span>2. Syntax & AST</span>
                </div>
                <span className="text-[10px] text-[#8b949e]">Parser</span>
              </button>

              <button
                onClick={() => handleStageRun('SEMANTIC', 'symbol')}
                disabled={isCompiling}
                className="w-full text-left flex items-center justify-between px-2 py-1.5 rounded hover:bg-[#21262d] text-[#c9d1d9] transition-colors"
              >
                <div className="flex items-center space-x-2">
                  <Table className="w-3.5 h-3.5 text-purple-400" />
                  <span>3. Semantic Check</span>
                </div>
                <span className="text-[10px] text-[#8b949e]">Symbols</span>
              </button>

              <button
                onClick={() => handleStageRun('IR', 'tac')}
                disabled={isCompiling}
                className="w-full text-left flex items-center justify-between px-2 py-1.5 rounded hover:bg-[#21262d] text-[#c9d1d9] transition-colors"
              >
                <div className="flex items-center space-x-2">
                  <Cpu className="w-3.5 h-3.5 text-amber-400" />
                  <span>4. Intermediate Code</span>
                </div>
                <span className="text-[10px] text-[#8b949e]">TAC/IR</span>
              </button>

              <div className="pt-1">
                <button
                  onClick={() => runCompilation('ALL')}
                  disabled={isCompiling}
                  className="w-full py-1.5 px-2 bg-indigo-600/20 hover:bg-indigo-600/30 border border-indigo-500/40 text-indigo-300 font-semibold rounded flex items-center justify-center gap-1.5 transition-colors"
                >
                  <Play className="w-3 h-3 fill-current" />
                  <span>Run Full Pipeline</span>
                </button>
              </div>
            </div>
          )}
        </div>

        {/* Section 3: Sample Programs Quick-Load */}
        <div>
          <div
            onClick={() => toggleSection('samples')}
            className="flex items-center justify-between text-[#8b949e] hover:text-white cursor-pointer py-1 px-1 rounded hover:bg-[#21262d]/40 transition-colors font-semibold"
          >
            <div className="flex items-center space-x-1">
              {collapsedSections.samples ? (
                <ChevronRight className="w-3.5 h-3.5" />
              ) : (
                <ChevronDown className="w-3.5 h-3.5" />
              )}
              <span className="uppercase text-[10px] tracking-wider">Example Programs</span>
            </div>
            <Sparkles className="w-3.5 h-3.5 text-amber-400" />
          </div>

          {!collapsedSections.samples && (
            <div className="mt-1 space-y-1 pl-2">
              {SAMPLE_PROGRAMS.map(prog => (
                <button
                  key={prog.id}
                  onClick={() => loadSampleProgram(prog)}
                  className="w-full text-left p-1.5 rounded hover:bg-[#21262d] text-[#c9d1d9] transition-colors border border-transparent hover:border-[#30363d]"
                >
                  <div className="font-medium text-[11px] text-indigo-300">{prog.name}</div>
                  <div className="text-[10px] text-[#8b949e] truncate">{prog.description}</div>
                </button>
              ))}
            </div>
          )}
        </div>
      </div>
    </aside>
  );
};
