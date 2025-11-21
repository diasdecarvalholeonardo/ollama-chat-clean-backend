import { useState, useRef, useEffect } from "react";
import { sendMessage } from "./api/chatService";
import "./App.css";

interface ChatMessage {
  role: "user" | "assistant";
  content: string;
}

export default function App() {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const chatEndRef = useRef<HTMLDivElement | null>(null);

  // Rolagem automática
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  async function handleSend() {
    if (!input.trim()) return;

    const userMessage = input;
    setInput("");

    // adiciona mensagem do usuário
    setMessages((old) => [...old, { role: "user", content: userMessage }]);
    setLoading(true);

    try {
      const result = await sendMessage(userMessage);

      // adiciona resposta da IA
      setMessages((old) => [
        ...old,
        {
          role: "assistant",
          content: result.reply ?? "⚠️ Resposta vazia do backend",
        },
      ]);
    } catch {
      setMessages((old) => [
        ...old,
        {
          role: "assistant",
          content: "❌ Erro ao comunicar com o backend.",
        },
      ]);
    }

    setLoading(false);
  }

  return (
    <div className="chat-container">
      <h1>Ollama Chat</h1>

      <div className="messages">
        {messages.map((msg, i) => (
          <div key={i} className={`msg ${msg.role}`}>
            <b>{msg.role === "user" ? "Você:" : "IA:"}</b> {msg.content}
          </div>
        ))}

        {loading && <div className="msg assistant">IA está digitando...</div>}

        <div ref={chatEndRef}></div>
      </div>

      <div className="input-area">
        <input
          value={input}
          placeholder="Digite uma mensagem..."
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(_e) => _e.key === "Enter" && handleSend()}
        />

        <button onClick={handleSend}>Enviar</button>
      </div>
    </div>
  );
}
