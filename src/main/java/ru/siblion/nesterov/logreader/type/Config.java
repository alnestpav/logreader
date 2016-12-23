package ru.siblion.nesterov.logreader.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * Created by alexander on 22.12.2016.
 */
@XmlRootElement(name = "log-files")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {
    @XmlElement(name = "directory")
    private File directory;

    @XmlElement(name = "life-time")
    private int lifeTime; // в секундах

    public Config() {

    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }
}
