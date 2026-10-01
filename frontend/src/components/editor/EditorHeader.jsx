import React from 'react';
import { Play, Save, Check, RefreshCw } from 'lucide-react';
import { useProject } from '../../context/ProjectContext';
import { useCompiler } from '../../context/CompilerContext';

export const EditorHeader = () => {
  const { activeFile, saveFile, autosaveEnabled, setAutosaveEnabled } = useProject();
  const { runCompilation, isCompiling } = useCompiler();

  if (!activeFile) return null;

  return (
    <div className="h-8 bg-[#161b22] border-b border-[#21262d] px-3 flex items-center justify-between text-xs text-[#8b949e]">
      <div className="flex items-center space-x-2">
        <span className="text-[#c9d1d9] font-mono text-[11px]">{activeFile.name}</span>
        {activeFile.isDirty && (
          <span className="text-[10px] text-amber-400 bg-amber-400/10 px-1.5 py-0.5 rounded border border-amber-400/20">
            Unsaved Changes
          </span>
        )}
      </div>

      <div className="flex items-center space-x-3">
        <label className="flex items-center space-x-1.5 cursor-pointer select-none text-[11px]">
          <input
            type="checkbox"
            checked={autosaveEnabled}
            onChange={(e) => setAutosaveEnabled(e.target.checked)}
            className="rounded bg-[#21262d] border-[#30363d] text-indigo-600 focus:ring-0 w-3 h-3"
          />
          <span>Autosave</span>
        </label>

        <span className="text-[10px] text-[#484f58]">Ctrl+Enter to Compile</span>
      </div>
    </div>
  );
};
