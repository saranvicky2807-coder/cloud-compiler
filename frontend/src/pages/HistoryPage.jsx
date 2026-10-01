import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  History, 
  Trash2, 
  Play, 
  CheckCircle2, 
  AlertCircle, 
  Clock, 
  Calendar, 
  Code, 
  Layers, 
  RefreshCw,
  Search,
  Filter
} from 'lucide-react';
import { Navbar } from '../components/layout/Navbar';
import { Footer } from '../components/layout/Footer';
import { projectService } from '../services/projectService';
import { useAuth } from '../context/AuthContext';
import { useProject } from '../context/ProjectContext';

export const HistoryPage = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const { loadSampleProgram } = useProject();
  const [historyList, setHistoryList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('ALL'); // ALL, SUCCESS, FAILED
  const [selectedRecord, setSelectedRecord] = useState(null);

  useEffect(() => {
    fetchHistory();
  }, [isAuthenticated]);

  const fetchHistory = async () => {
    if (isAuthenticated) {
      try {
        setLoading(true);
        const data = await projectService.getHistory();
        setHistoryList(data);
        if (data.length > 0 && !selectedRecord) {
          setSelectedRecord(data[0]);
        }
      } catch (err) {
        console.error('Failed to load history:', err);
      } finally {
        setLoading(false);
      }
    } else {
      setLoading(false);
    }
  };

  const handleDeleteHistory = async (id) => {
    try {
      await projectService.deleteHistory(id);
      setHistoryList(prev => prev.filter(h => h.id !== id));
      if (selectedRecord?.id === id) {
        setSelectedRecord(null);
      }
    } catch (err) {
      console.error('Failed to delete record:', err);
    }
  };

  const handleClearAll = async () => {
    if (window.confirm('Are you sure you want to clear all compilation history?')) {
      try {
        await projectService.clearHistory();
        setHistoryList([]);
        setSelectedRecord(null);
      } catch (err) {
        console.error('Failed to clear history:', err);
      }
    }
  };

  const handleLoadInEditor = (record) => {
    loadSampleProgram({
      id: 'history-' + record.id,
      name: record.fileName || 'recovered_source.c',
      code: record.sourceCode || ''
    });
    navigate('/');
  };

  const filteredHistory = historyList.filter(h => {
    const matchesSearch = 
      h.projectName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      h.fileName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      h.summary?.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesStatus = 
      filterStatus === 'ALL' ||
      (filterStatus === 'SUCCESS' && h.success) ||
      (filterStatus === 'FAILED' && !h.success);

    return matchesSearch && matchesStatus;
  });

  return (
    <div className="h-screen w-screen flex flex-col bg-[#0d1117] text-[#c9d1d9] overflow-hidden">
      <Navbar />

      <div className="flex-1 overflow-hidden p-6 max-w-7xl w-full mx-auto flex flex-col space-y-4">
        {/* Top Controls */}
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div>
            <h1 className="text-xl font-bold text-white flex items-center gap-2">
              <History className="w-5 h-5 text-indigo-400" />
              Compilation Runs & History
            </h1>
            <p className="text-xs text-[#8b949e] mt-0.5">
              Review past compiler executions, diagnostic summaries, and reload code snapshots.
            </p>
          </div>

          {isAuthenticated && historyList.length > 0 && (
            <button
              onClick={handleClearAll}
              className="px-3 py-1.5 bg-rose-600/20 hover:bg-rose-600/30 text-rose-300 border border-rose-500/30 rounded text-xs font-semibold flex items-center gap-1.5 transition-colors"
            >
              <Trash2 className="w-3.5 h-3.5" />
              <span>Clear All History</span>
            </button>
          )}
        </div>

        {/* Filter Bar */}
        <div className="p-3 bg-[#161b22] border border-[#21262d] rounded-lg flex flex-wrap items-center justify-between gap-3 text-xs">
          <div className="flex items-center space-x-2 flex-1 min-w-[200px]">
            <div className="relative flex-1">
              <Search className="w-3.5 h-3.5 absolute left-2 top-2.5 text-[#8b949e]" />
              <input
                type="text"
                placeholder="Search history by project or file..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full bg-[#0d1117] text-[#c9d1d9] text-xs rounded pl-7 pr-2 py-1.5 border border-[#30363d] focus:outline-none focus:border-indigo-500"
              />
            </div>

            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="bg-[#0d1117] text-[#c9d1d9] text-xs rounded px-2.5 py-1.5 border border-[#30363d] focus:outline-none focus:border-indigo-500"
            >
              <option value="ALL">All Statuses ({historyList.length})</option>
              <option value="SUCCESS">Successful Runs Only</option>
              <option value="FAILED">Failed Runs Only</option>
            </select>
          </div>

          <button
            onClick={fetchHistory}
            className="p-1.5 bg-[#21262d] hover:bg-[#30363d] rounded border border-[#30363d] text-[#8b949e] hover:text-white transition-colors"
            title="Refresh"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${loading ? 'animate-spin' : ''}`} />
          </button>
        </div>

        {/* Two-Column History Layout */}
        <div className="flex-1 overflow-hidden grid grid-cols-1 md:grid-cols-12 gap-4">
          {/* List of Runs */}
          <div className="md:col-span-5 bg-[#161b22] border border-[#21262d] rounded-lg overflow-y-auto divide-y divide-[#21262d]">
            {loading ? (
              <div className="p-8 text-center text-[#8b949e] text-xs">
                Loading compilation history...
              </div>
            ) : filteredHistory.length === 0 ? (
              <div className="p-8 text-center text-[#8b949e] text-xs">
                <p>No compilation records found.</p>
                <p className="text-[11px] text-[#484f58] mt-1">
                  {isAuthenticated
                    ? 'Compile code from the IDE to track history logs here.'
                    : 'Sign in to persist your compiler history runs.'}
                </p>
              </div>
            ) : (
              filteredHistory.map((item) => {
                const isSelected = selectedRecord?.id === item.id;
                return (
                  <div
                    key={item.id}
                    onClick={() => setSelectedRecord(item)}
                    className={`p-3 cursor-pointer transition-colors ${
                      isSelected
                        ? 'bg-indigo-600/10 border-l-2 border-l-indigo-500'
                        : 'hover:bg-[#0d1117]/50'
                    }`}
                  >
                    <div className="flex items-center justify-between text-xs font-semibold mb-1">
                      <span className="text-white flex items-center gap-1.5">
                        {item.success ? (
                          <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400 shrink-0" />
                        ) : (
                          <AlertCircle className="w-3.5 h-3.5 text-rose-400 shrink-0" />
                        )}
                        <span className="truncate">{item.projectName || 'Playground'} / {item.fileName}</span>
                      </span>

                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          handleDeleteHistory(item.id);
                        }}
                        className="p-1 text-[#484f58] hover:text-rose-400 transition-colors"
                        title="Delete Record"
                      >
                        <Trash2 className="w-3 h-3" />
                      </button>
                    </div>

                    <div className="text-[11px] text-[#8b949e] flex items-center justify-between">
                      <span>{new Date(item.createdAt).toLocaleString()}</span>
                      <span className="text-amber-300 font-mono">{item.executionTimeMs}ms</span>
                    </div>

                    <div className="text-[11px] text-[#8b949e] mt-1 truncate">
                      {item.summary}
                    </div>
                  </div>
                );
              })
            )}
          </div>

          {/* Details / Snapshot Viewer */}
          <div className="md:col-span-7 bg-[#161b22] border border-[#21262d] rounded-lg p-4 flex flex-col overflow-hidden">
            {selectedRecord ? (
              <div className="flex-1 flex flex-col overflow-hidden space-y-3">
                <div className="flex items-center justify-between pb-2 border-b border-[#21262d]">
                  <div>
                    <h3 className="text-xs font-bold text-white flex items-center gap-2">
                      <Code className="w-4 h-4 text-indigo-400" />
                      Snapshot: {selectedRecord.fileName} ({selectedRecord.projectName || 'Playground'})
                    </h3>
                    <div className="text-[11px] text-[#8b949e] mt-0.5">
                      Executed at {new Date(selectedRecord.createdAt).toLocaleString()}
                    </div>
                  </div>

                  <button
                    onClick={() => handleLoadInEditor(selectedRecord)}
                    className="px-3 py-1.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded text-xs font-semibold flex items-center gap-1.5 transition-colors"
                  >
                    <Play className="w-3.5 h-3.5 fill-current" />
                    <span>Load in Editor</span>
                  </button>
                </div>

                <div className="grid grid-cols-3 gap-2 text-xs">
                  <div className="p-2 bg-[#0d1117] rounded border border-[#21262d]">
                    <span className="text-[10px] text-[#8b949e] block">Status</span>
                    <span className={`font-bold ${selectedRecord.success ? 'text-emerald-400' : 'text-rose-400'}`}>
                      {selectedRecord.success ? 'SUCCESS' : 'FAILED'}
                    </span>
                  </div>

                  <div className="p-2 bg-[#0d1117] rounded border border-[#21262d]">
                    <span className="text-[10px] text-[#8b949e] block">Errors</span>
                    <span className="text-white font-bold">{selectedRecord.totalErrors}</span>
                  </div>

                  <div className="p-2 bg-[#0d1117] rounded border border-[#21262d]">
                    <span className="text-[10px] text-[#8b949e] block">Execution Time</span>
                    <span className="text-amber-300 font-bold">{selectedRecord.executionTimeMs} ms</span>
                  </div>
                </div>

                <div className="flex-1 flex flex-col overflow-hidden">
                  <span className="text-[11px] font-semibold text-[#8b949e] mb-1">Source Code Snapshot:</span>
                  <div className="flex-1 bg-[#0d1117] p-3 rounded border border-[#30363d] overflow-auto font-mono text-xs text-[#c9d1d9] select-text">
                    <pre>{selectedRecord.sourceCode}</pre>
                  </div>
                </div>
              </div>
            ) : (
              <div className="h-full flex items-center justify-center text-[#8b949e] text-xs">
                Select a compilation run on the left to view details and code snapshot.
              </div>
            )}
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};
