#!/usr/bin/env python

try:
  import pygtk
  pygtk.require('2.0')
except:
  pass
try:
  import gtk
  import gtk.glade
except:
  print('GTK not available')
  sys.exit(1)

class texto:
  
  def __init__(self):
    self.gladefile = "/home/mod/GUI/GUI_STEP-NC_Viewer.ui"
    self.builder = gtk.Builder()
    self.builder.add_from_file(self.gladefile)
    self.window1 = self.builder.get_object("window1")
    self.window2 = self.builder.get_object("window2")
    self.textview1 = self.builder.get_object("textview1")
    self.button1 = self.builder.get_object("button1")
    self.button2 = self.builder.get_object("button2")
    self.button3 = self.builder.get_object("button3")
    self.builder.connect_signals(self)
    self.buffertexto = gtk.TextBuffer()
    self.lista = []
    self.archivo_name = "/home/testefinal/Temp/Code_original.txt"
    self.lista = open(self.archivo_name,"r").readlines()
    for i in range(len(self.lista)):
          self.buffertexto.insert_at_cursor(self.lista[i])
    self.textview1.set_buffer(self.buffertexto)
    self.x = 715
    self.y = 0    
    self.window1.move(self.x, self.y)
    self.window1.show()
    self.window2.hide()
    self.button1.hide()

  def on_button1_clicked(self, widget):
    self.archivo_name = "/home/testefinal/Temp/Code_original.txt"
    self.lista = open(self.archivo_name,"r").readlines()
    for i in range(len(self.lista)):
          self.buffertexto.insert_at_cursor(self.lista[i])
    self.textview1.set_buffer(self.buffertexto)
    self.button1.hide()
    self.button2.show()

  def on_button2_clicked(self, widget):
    self.x2 = 715
    self.y2 = 0   
    self.window2.move(self.x2, self.y2)
    self.window2.show()
    self.window1.hide()

  def on_button3_clicked(self, widget):
    self.x = 715
    self.y = 0  
    self.window1.move(self.x, self.y)
    self.window1.show()
    self.window2.hide()

  def on_window1_destroy(self, object, data=None):
    #print "quit with cancel"
    gtk.main_quit()

  def on_window2_destroy(self, object, data=None):
    #print "quit with cancel"
    gtk.main_quit()


if __name__ == "__main__":
  main = texto()
  gtk.main()
