import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class BasketItem extends MaxObject{
	private String filePath;
	private MaxBox playlistObject;
	private SymbolButton symButton;
	private MaxBox dict;
	private MaxBox dictListenerButton;
	private MaxBox dictListener;
	private MaxBox audioPatcherReceiverLeft;
	private MaxBox audioPatcherReceiverRight;
	
	
	public BasketItem(String filePath, MaxPatcher parentPatcher, int itemNumber){
		this.filePath  =filePath;
		audioPatcherReceiverLeft  = parentPatcher.getNamedBox("audioPatcherReceiverLeft");
		audioPatcherReceiverRight =  parentPatcher.getNamedBox("audioPatcherReceiverRight");
			
		int xPosition = 0;
		int yPosition;
		if(itemNumber== 0){
			yPosition = 0;
		}else{
			yPosition = itemNumber*50;
		}
		
		symButton = new SymbolButton(parentPatcher,xPosition, yPosition ,filePath);
		
		playlistObject = parentPatcher.newDefault( xPosition+50, yPosition, "playlist~",null);
		playlistObject.send("size", new Atom [] { Atom.newAtom(400), Atom.newAtom (50)});
		playlistObject.send("append", new Atom [] { Atom.newAtom(filePath)});
		playlistObject.send("waveformdisplay", new Atom [] { Atom.newAtom(0)});
		playlistObject.send("clipheight", new Atom [] { Atom.newAtom(50)});
		playlistObject.send("allowreorder", new Atom [] { Atom.newAtom(0)});
		playlistObject.send("presentation", new Atom[] {Atom.newAtom(true)});
		//connect the audio outputs
		parentPatcher.connect(playlistObject, 0,audioPatcherReceiverLeft , 0);
		parentPatcher.connect(playlistObject, 1,audioPatcherReceiverRight , 0);
		
		dict  = parentPatcher.newDefault(xPosition +600, yPosition,"dict", null);
		parentPatcher.connect(playlistObject, 4, dict, 0);
		dict.send("name", new Atom[]{Atom.newAtom(filePath)});
		
		dictListenerButton = parentPatcher.newDefault(xPosition +700, yPosition, "button", null);
		parentPatcher.connect(dict, 0, dictListenerButton, 0);
		
		dictListener = parentPatcher.newDefault(xPosition +800, yPosition, "js",  new Atom []{Atom.newAtom("dictListener")});
		parentPatcher.connect( dictListenerButton, 0, dictListener, 0);
		dictListener.send("setFilePathAndDict", new Atom []{Atom.newAtom(filePath)});
		
		
	}


	public MaxBox getPlaylistObject() {
		return playlistObject;
	}


	public void setPlaylistObject(MaxBox playlistObject) {
		this.playlistObject = playlistObject;
	}


	public SymbolButton getSymButton() {
		return symButton;
	}


	public void setSymButton(SymbolButton symButton) {
		this.symButton = symButton;
	}


	public MaxBox getDict() {
		return dict;
	}


	public void setDict(MaxBox dict) {
		this.dict = dict;
	}


	public MaxBox getDictListenerButton() {
		return dictListenerButton;
	}


	public void setDictListenerButton(MaxBox dictListenerButton) {
		this.dictListenerButton = dictListenerButton;
	}


	public MaxBox getDictListener() {
		return dictListener;
	}


	public void setDictListener(MaxBox dictListener) {
		this.dictListener = dictListener;
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
