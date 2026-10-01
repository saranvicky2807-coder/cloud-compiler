import React from 'react';
import { FileCode, X, Plus } from 'lucide-react';
import { useProject } from '../../context/ProjectContext';

export const FileTabBar = ({ onOpenNewFile }) => {
  const { openFiles, activeFileId, setActiveFileId, closeFileTab } = useProject();

  return (
    <div className="h-9 bg-[#0d1117] border-b border-[#21262d] flex items-center justify-between overflow-x-auto select-none">
      <div className="flex items-center h-full">
        {openFiles.map((file) => {
          const isActive = file.id === activeFileId;
          return (
            <div
              key={file.id}
              onClick={() => setActiveFileId(file.id)}
              className={`h-full flex items-center space-x-2 px-3 border-r border-[#21262d] cursor-pointer text-xs transition-colors group ${
                isActive
                  ? 'bg-[#161b22] text-white border-t-2 border-t-indigo-500 font-medium'
                  : 'text-[#8b949e] hover:bg-[#161b22]/50 hover:text-[#c9d1d9]'
              }`}
            >
              <FileCode className={`w-3.5 h-3.5 ${isActive ? 'text-indigo-400' : 'text-[#8b949e]'}`} />
              <span className="truncate max-w-[130px]">{file.name}</span>

              {file.isDirty ? (
                <span className="w-2 h-2 rounded-full bg-amber-400 group-hover:hidden" title="Unsaved changes" />
              ) : null}

              {openFiles.length > 1 && (
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    closeFileTab(file.id);
                  }}
                  className={`p-0.5 rounded hover:bg-[#30363d] text-[#8b949e] hover:text-white ${
                    file.isDirty ? 'hidden group-hover:block' : ''
                  }`}
                  title="Close tab"
                >
                  <X className="w-3 h-3" />
                </button>
              )}
            </div>
          );
        })}
      </div>

      <button
        onClick={onOpenNewFile}
        className="px-2.5 h-full text-[#8b949e] hover:text-white hover:bg-[#161b22] transition-colors border-l border-[#21262d] flex items-center justify-center"
        title="New File"
      >
        <Plus className="w-3.5 h-3.5" />
      </button>
    </div>
  );
};
