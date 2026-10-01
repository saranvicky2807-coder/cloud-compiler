import React from 'react';
import { X, Sparkles, Code, ArrowRight } from 'lucide-react';
import { SAMPLE_PROGRAMS } from '../../utils/samplePrograms';
import { useProject } from '../../context/ProjectContext';

export const TemplatesModal = ({ isOpen, onClose }) => {
  const { loadSampleProgram } = useProject();

  if (!isOpen) return null;

  const handleSelect = (sample) => {
    loadSampleProgram(sample);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-[#161b22] border border-[#30363d] rounded-lg max-w-xl w-full p-4 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#21262d]">
          <div className="flex items-center space-x-2 text-sm font-semibold text-white">
            <Sparkles className="w-4 h-4 text-amber-400" />
            <span>Select Compiler Example Template</span>
          </div>
          <button onClick={onClose} className="text-[#8b949e] hover:text-white">
            <X className="w-4 h-4" />
          </button>
        </div>

        <div className="mt-3 space-y-2 max-h-[60vh] overflow-y-auto pr-1">
          {SAMPLE_PROGRAMS.map((sample) => (
            <div
              key={sample.id}
              onClick={() => handleSelect(sample)}
              className="p-3 rounded-lg bg-[#0d1117] border border-[#21262d] hover:border-indigo-500/50 hover:bg-[#161b22] cursor-pointer transition-all group"
            >
              <div className="flex items-center justify-between">
                <div className="text-xs font-semibold text-indigo-300 group-hover:text-indigo-200">
                  {sample.name}
                </div>
                <ArrowRight className="w-3.5 h-3.5 text-[#8b949e] group-hover:text-white transition-colors" />
              </div>
              <div className="text-[11px] text-[#8b949e] mt-1">
                {sample.description}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
