/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package cn.finder.wae.waepp.server.net;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.io.XMPPPacketReader;

/** 
 * This class is to handle incoming JSON stanzas.
 *
 * @author WHL 
 */
public class StanzaHandler {

    private static final Logger log = Logger.getLogger(StanzaHandler.class);


    protected String serverName;

    private boolean sessionCreated = false;

    private boolean startedTLS = false;


    /**
     * Constructor.
     * 
     * @param serverName the server name
     * @param connection the connection
     */
    public StanzaHandler() {
    }

    /**
     * Process the received stanza using the given XMPP packet reader.
     *  
     * @param stanza the received statza
     * @param reader the XMPP packet reader
     * @throws Exception if the XML stream is not valid.
     */
    public void process(String stanza, XMPPPacketReader reader)
            throws Exception {

    }

    private void processMessage(Element doc) {
    }

    private void processPresence(Element doc) {

    }

   
}
