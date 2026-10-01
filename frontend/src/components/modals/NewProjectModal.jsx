import React, { useState } from 'react';
import { X, FolderPlus } from 'lucide-react';
import { useProject } from '../../context/ProjectContext';

export const NewProjectModal = ({ isOpen, onClose }) => {
  const { createProject } = useProject();
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name.trim()) {
      setError('Project name is required');
      return;
    }

    setLoading(true);
    setError('');
    try {
      await createProject(name.trim(), description.trim());
      setName('');
      setDescription('');
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create project');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-[#161b22] border border-[#30363d] rounded-lg max-w-md w-full p-4 shadow-2xl">
        <div className="flex items-center justify-between pb-3 border-b border-[#21262d]">
          <div className="flex items-center space-x-2 text-sm font-semibold text-white">
            <FolderPlus className="w-4 h-4 text-indigo-400" />
            <span>Create New Compiler Project</span>
          </div>
          <button onClick={onClose} className="text-[#8b949e] hover:text-white">
            <X className="w-4 h-4" />
          </button>
        </div>

        {error && (
          <div className="mt-3 p-2 rounded bg-rose-500/10 border border-rose-500/30 text-rose-300 text-xs">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="mt-3 space-y-3 text-xs">
          <div>
            <label className="block text-[#8b949e] mb-1 font-medium">Project Name</label>
            <input
              type="text"
              placeholder="e.g. MyCompilerLab"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full bg-[#0d1117] text-white p-2 rounded border border-[#30363d] focus:outline-none focus:border-indigo-500"
              autoFocus
            />
          </div>

          <div>
            <label className="block text-[#8b949e] mb-1 font-medium">Description (Optional)</label>
            <textarea
              rows={3}
              placeholder="Project goals or notes..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full bg-[#0d1117] text-white p-2 rounded border border-[#30363d] focus:outline-none focus:border-indigo-500 resize-none"
            />
          </div>

          <div className="flex justify-end space-x-2 pt-2 border-t border-[#21262d]">
            <button
              type="button"
              onClick={onClose}
              className="px-3 py-1.5 rounded bg-[#21262d] text-[#c9d1d9] hover:bg-[#30363d] transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="px-3 py-1.5 rounded bg-indigo-600 hover:bg-indigo-500 text-white font-medium disabled:opacity-50 transition-colors"
            >
              {loading ? 'Creating...' : 'Create Project'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
